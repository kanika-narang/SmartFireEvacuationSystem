from django.shortcuts import render
from django.http import HttpResponse
from django.template.loader import render_to_string
import json,requests
# Create your views here.

def index(request):
    print "hello"
    getSensorData()
    return render(request, "index.html")

def getSensorData():
	url="http://172.16.88.22:8000/api/getdata/"
	data={}
	data["sensor_token"]="hrlqpjysw0lcqls0"
	data["device_token"]="8iu2be6yn7sg5plq"
	data["count"]="10"
	r=requests.post(url,data=data)
	print r.json()
	jsonObject=r.json()
	return jsonObject
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
	if request.method == 'POST':
		try:
			print "good"
		except Exception, e:
			error["custom"] =  str(e)
	HTMLResponse="hello kanika"
	responseMessage['htmlresponse'] =HTMLResponse 
	datatoshow=getSensorData()
	context["datatoshow"]=datatoshow
	HTMLResponse = render_to_string('datatable.html', context= context)
	responseMessage['datatoshow']=HTMLResponse
	responseMessage['error'] = error
	return HttpResponse(str(json.dumps(responseMessage)))    	
	return 0

