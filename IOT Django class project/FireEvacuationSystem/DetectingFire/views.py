from django.shortcuts import render
from django.http import HttpResponse
from django.template.loader import render_to_string
import json,requests
from django.views.decorators.csrf import csrf_exempt
# Create your views here.

flamedata={}
smokedata={}
tempdata={}
userdata={}
prevflamedata={}
prevtempdata={}
prevsmokedata={}
prevuserdata={}

tempavg=0
prevtempavg=0
flameavg=0
smokeavg=0
tempans=0

temp=0
fire=0
miss=0

showcount=0


def index(request):
    print "hello"
   # getSensorData()
    return render(request, "index.html")


@csrf_exempt
def getSensorData(callto):
	url="http://theiotdashboard.tk/api/getsensordata/"
	data={}
	if str(callto)== "user":
		data["sensor_token"]="rjxtcmzrm0sr015k"
		data["device_token"]="bu81mm3rzg09ldt3"
		print("user.............")

	elif str(callto)=="smoke":
		#data["sensor_token"]="fj836iw91yfzbw75"
		#data["device_token"]="sya0c85x69fwl9ol"
		data["sensor_token"]="9x2brkrkkih2mlpl"
		data["device_token"]="r7c43wt3sbt7gs5n"
		print("smoke,.................")
	
	elif str(callto)=="flame":
		# data["sensor_token"]="c8tp2oi3ik7um78a"
		# data["device_token"]="sya0c85x69fwl9ol"
		data["sensor_token"]="524l7cbscmbf8quo"
		data["device_token"]="r7c43wt3sbt7gs5n"
		print "flame.................."

	elif str(callto)=="temp":
		# data["sensor_token"]="kw5rzbnulrqs1x22"
		# data["device_token"]="sya0c85x69fwl9ol"
		data["sensor_token"]="btfzzaumru7s0g9y"
		data["device_token"]="bu81mm3rzg09ldt3"
		print("temp,.................")	
	
	data["num_records"]="10"
	r=requests.post(url,data=data)
	#print r.json()
	#jsonObject=r.json()
	resp_obj=json.loads(r.json())
	# header = resp_obj.get('header',[]) # Header is list
	# error = resp_obj.get('error',dict()) # Error is dict
	# data = resp_obj.get('data',[]) # Data is two-dimensional list
	# print data
	# #data= jsonObject.get('data',[]) 
	# print "goooooo"
	if str(callto)=="temp":
		global tempdata
		tempdata=resp_obj
		#print "tempdata"+str(tempdata)
	elif str(callto)=="smoke":	
		global smokedata
		smokedata=resp_obj
		#print "smokedata"+str(smokedata)
	elif str(callto)=="flame":
		global flamedata
		flamedata=resp_obj
		#print "flamedata"+str(flamedata)
	elif str(callto)=="user":
		global userdata
		userdata=resp_obj
		#print "userdata"+str(userdata)
		
		
	return resp_obj
	# print jsonObject[1]['ServerTimestamp']
	# print jsonObject[1]['temperature']
	# print jsonObject[1]['humidity']

def getDataToShow(request):
	print "here"
	HTMLResponse = ""
	datatoshow=""
	responseMessage = {}
	error = {}
	context = {}
	print "inside"
	callto=request.GET["callto"]
	print callto
	try:
		print "good"
	except Exception, e:
		error["custom"] =  str(e)
	#responseMessage['htmlresponse'] =HTMLResponse 
	datatoshow=getSensorData(callto)
	logic()
	header = datatoshow.get('header',[]) # Header is list
	context["header"]=header
	error = datatoshow.get('error',dict()) # Error is dict
	data = datatoshow.get('data',[]) # Data is two-dimensional list
	print "got data"
	context["datatoshow"]=data
	context["callto"]=callto
	# print "nice"
	HTMLResponse = render_to_string('datatable.html',context)
	responseMessage['datatoshow']=HTMLResponse
	responseMessage['error'] = error
	return HttpResponse(str(json.dumps(responseMessage)))    	
	return 0

def firealarm():
	print "fire"


def missalarm():
	print "miss Happening"

def logic():
	print "logiclllllllllllllllllllllllllll"
	global prevtempdata
	global prevsmokedata
	global prevflamedata
	global tempavg
	global smokeavg
	global flameavg
	global fire
	global miss
	global temp
	global prevtempavg
	global tempans
	if prevtempdata!=tempdata:
		data = tempdata.get('data',[]) # Data is two-dimensional list
		prevtempdata=tempdata
		if data!=[]:
			sum=0
			for i in data:
				sum=sum+int(i[0])
			tempavg= sum/10
			if prevtempavg<tempavg:
				temp=temp+1
			else:
				temp=0		
			prevtempavg=tempavg	
			print "tempavg="+str(tempavg)
	else:
		print "same as last"		
	if prevsmokedata!=smokedata:
		data = smokedata.get('data',[]) # Data is two-dimensional list
		prevsmokedata=smokedata
		if data!=[]:
			sum=0
			for i in data:
				sum=sum+int(i[0])
			smokeavg= sum/10
			print "smokeavg="+str(smokeavg)
	else:
		print "same as last"		
	if prevflamedata!=flamedata:
		data = flamedata.get('data',[]) # Data is two-dimensional list
		prevflamedata=flamedata
		if data!=[]:
			sum=0
			for i in data:
				sum=sum+int(i[0])
			flameavg= sum/10
			print "flameavg="+str(flameavg)
	else:
		print "same as last"

	# logic starts	
	if temp>=5:
		tempans=1
		temp=0;
	else:
		tempavg=0		
	if flameavg==1:
		if smokeavg==1:
			fire=1
		else:
			miss=1
	else:
		if smokeavg==1:
			if tempans==1:
				fire=1
			else:
				miss=1

	print "miss"+str(miss)
	print "fire"+str(fire)
	if fire==1:
		toSendData(fire)
		firealarm()
	elif miss==1:
		missalarm()
	else:
		print "all OKKKKKKKKK"		
	print "Done logic ................"


def getFIREorMissdetail(request):
	global fire
	global miss
	responseMessage = {}
	responseMessage['fire']=fire
	responseMessage['miss'] = miss
	return HttpResponse(str(json.dumps(responseMessage)))    


def toSendData(datatosend):
	print "sending................"
	url="http://theiotdashboard.tk/api/send/"
	data={}
	data["sensor_token"]="bzr4f033oy97md7e"
	data["device_token"]="sya0c85x69fwl9ol"
	data["Fire"]=datatosend
	headers = {'Content-Type':'application/json'}
	resp = requests.post(url,data=json.dumps(data),headers=headers)
	print "sent-----------------------"