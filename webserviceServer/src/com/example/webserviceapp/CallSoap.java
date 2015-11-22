package com.example.webserviceapp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CallSoap 
{
	public final String SOAP_ACTION = "http://tempuri.org/registerUser";

	public  final String OPERATION_NAME = "registerUser"; 

	public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

	public  final String SOAP_ADDRESS = "http://192.168.1.104:9012/AndroidSecurity.asmx";
	boolean istrue=false;
	public CallSoap() 
	{ 
	}
	public String Call(String regKey,String UserId,String password)
	{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
	PropertyInfo pi=new PropertyInfo();
	pi.setName("regKey");
	        pi.setValue(regKey);
	        pi.setType(String.class);
	        request.addProperty(pi);
	        pi=new PropertyInfo();
	        pi.setName("UserId");
	        pi.setValue(UserId);
	        pi.setType(Integer.class);
	        request.addProperty(pi);
	        pi=new PropertyInfo();
	        pi.setName("password");
	        pi.setValue(password);
	        pi.setType(Integer.class);
	        request.addProperty(pi);
	        

	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet = true;

	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	try
	{
	httpTransport.call(SOAP_ACTION, envelope);
	response = envelope.getResponse();
	}
	catch (Exception exception)
	{
	response=exception.toString();
	}
	return response.toString();
	}
}
