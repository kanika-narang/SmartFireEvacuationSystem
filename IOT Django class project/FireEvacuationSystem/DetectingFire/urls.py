from django.conf.urls import url
from django.conf import settings
from django.conf.urls.static import static
from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^get_data_toshow/$' , views.getDataToShow, name='getDataToShow'),
    url(r'^getFIREorMissdetail/$' , views.getFIREorMissdetail, name='getFIREorMissdetail'),
    # url(r'^get_data_toshowSmoke/$' , views.getDataToShowSmoke, name='getDataToShowSmoke'),
    # url(r'^get_data_toshowFlame/$' , views.getDataToShowFlame, name='getDataToShowFlame'),
    # url(r'^get_data_toshowTemp/$' , views.getDataToShowTemp, name='getDataToShowTemp'),
]
if settings.DEBUG:
	urlpatterns += static(settings.STATIC_URL,document_root=settings.STATIC_ROOT)