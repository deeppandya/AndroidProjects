package com.example.weatherdisplay;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class CallSoap 
{	
	public final String PT_SOAP_ACTION_FOR_REGISTERUSER = "http://tempuri.org/RegisterUser";
	public final String PT_OPERATION_NAME_FOR_REGISTERUSER = "RegisterUser";
	
	public final String PT_SOAP_ACTION_FOR_USER_LOGIN = "http://tempuri.org/CheckUser";
	public final String PT_OPERATION_NAME_FOR_USER_LOGIN = "CheckUser";
	
	public final String PT_SOAP_ACTION_FOR_ADD_PROJECT = "http://tempuri.org/AddProject";
	public final String PT_OPERATION_NAME_FOR_ADD_PROJECT = "AddProject";
	
	public final String PT_SOAP_ACTION_FOR_EDIT_PROJECT = "http://tempuri.org/UpdateProject";
	public final String PT_OPERATION_NAME_FOR_EDIT_PROJECT = "UpdateProject";
	
	public final String PT_SOAP_ACTION_FOR_GET_PROJECT = "http://tempuri.org/GetProjects";
	public final String PT_OPERATION_NAME_FOR_GET_PROJECT = "GetProjects";
	
	public final String PT_SOAP_ACTION_FOR_GET_PROJECT_TYPE = "http://tempuri.org/GetProject_Type_Id";
	public final String PT_OPERATION_NAME_FOR_GET_PROJECT_TYPE = "GetProject_Type_Id";
	
	public final String PT_SOAP_ACTION_FOR_GET_COMPANY_LIST = "http://tempuri.org/GetCompany";
	public final String PT_OPERATION_NAME_FOR_GET_COMPANY_LIST = "GetCompany";
	public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
	public final String PT_SOAP_ACTION_FOR_GET_COMPANY_CLIENT_LIST = "http://tempuri.org/GetCompanyClients";
	public final String PT_OPERATION_NAME_FOR_GET_CLIENT_LIST = "GetCompanyClients";
	
	//public  final String SOAP_ADDRESS = "http://60.254.50.157:9012/AndroidWS/AndroidSecurity.asmx";
	//public final String SOAP_ADDRESS = "http://192.168.0.90:9015/Projecttracker.asmx";
	//http://localhost:54992/Projecttracker.asmx
	public  final String SOAP_ADDRESS = "http://192.168.1.104:9018/Projecttracker.asmx";
	boolean istrue=false;
	
	SharedPreferences pref;
	Editor editor;
	String current_user_id = "";
	public CallSoap() 
	{ 
	}
	//------ WebService call for Registration------

//METHODS FOR PROJECT TRACKER
public String RegisterUser(String email_id, String password, String first_name, String last_name, String abbreviation)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_REGISTERUSER);
	PropertyInfo pi=new PropertyInfo();
	
	//adding values 
	pi.setName("UserId");
	pi.setValue(email_id);
	pi.setType(String.class);
	request.addProperty(pi);
	
	pi=new PropertyInfo();
	pi.setName("password");
	pi.setValue(password);
	pi.setType(String.class);
	request.addProperty(pi);
	
	pi=new PropertyInfo();
	pi.setName("fname");
	pi.setValue(first_name);
	pi.setType(String.class);
	request.addProperty(pi);
	
	pi=new PropertyInfo();
	pi.setName("lname");
	pi.setValue(last_name);
	pi.setType(String.class);
	request.addProperty(pi);
	
	pi=new PropertyInfo();
	pi.setName("aabv");
	pi.setValue(abbreviation);
	pi.setType(String.class);
	request.addProperty(pi);
	
	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet = true;
	
	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	
	try
	{
		httpTransport.call(PT_SOAP_ACTION_FOR_REGISTERUSER, envelope);
		response = envelope.getResponse();
	}
	catch (Exception exception)
	{
		response=exception.toString();
	}
		return response.toString();
	}

public String CheckUserLogin(String email_id, String password)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_USER_LOGIN);
	PropertyInfo pi=new PropertyInfo();
	        pi.setName("UserId");
	        pi.setValue(email_id);
	        pi.setType(String.class);
	        request.addProperty(pi);
	        pi=new PropertyInfo();
	        pi.setName("password");
	        pi.setValue(password);
	        pi.setType(String.class);
	        request.addProperty(pi);
	        
	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet = true;

	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	try
	{
	   	httpTransport.call(PT_SOAP_ACTION_FOR_USER_LOGIN, envelope);
	   	response = envelope.getResponse();
	}
	catch (Exception exception)
	{
	   	response=exception.toString();
	}
	  	return response.toString();
	}

public String AddProject(String company_id,String clientId, String bug_id, String project_type, 
		String start_time, String end_time, String description, String actions_taken,String current_user_id, String contact_person)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_ADD_PROJECT);
	PropertyInfo pi=new PropertyInfo();

		pi = new PropertyInfo();
		pi.setName("company_id");
		pi.setValue(company_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
		pi = new PropertyInfo();
		pi.setName("clientId");
		pi.setValue(clientId);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("Bugid");
	    pi.setValue(bug_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("Project_type_id");
	    pi.setValue(project_type);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("start_st");
	    pi.setValue(start_time);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("end_dt");
	    pi.setValue(end_time);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("bugdesc");
	    pi.setValue(description);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("action_taken");
	    pi.setValue(actions_taken);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("user_id");
	    pi.setValue(current_user_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("contact_person");
	    pi.setValue(contact_person);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response=null;
		
		try
		{
			httpTransport.call(PT_SOAP_ACTION_FOR_ADD_PROJECT, envelope);
			response = envelope.getResponse();
		}
		catch (Exception exception)
		{
			response=exception.toString();
		}
			return response.toString();
	}

public String EditProject(String company_id,String clientId, String bug_id, String project_type, 
		String start_time, String end_time, String description, String actions_taken, String contact_person, String session_id, String current_user_id)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_EDIT_PROJECT);
	PropertyInfo pi=new PropertyInfo();
		pi = new PropertyInfo();
		pi.setName("COMPANY_ID");
		pi.setValue(company_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
		pi = new PropertyInfo();
		pi.setName("CLIENT_ID");
		pi.setValue(clientId);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("Bugid");
	    pi.setValue(bug_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("Project_type_id");
	    pi.setValue(project_type);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("start_st");
	    pi.setValue(start_time);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("end_dt");
	    pi.setValue(end_time);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("bugdesc");
	    pi.setValue(description);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("action_taken");
	    pi.setValue(actions_taken);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("contact_person");
	    pi.setValue(contact_person);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("session_id");
	    pi.setValue(session_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    pi=new PropertyInfo();
	    pi.setName("user_id");
	    pi.setValue(current_user_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	    
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response=null;
		
		try
		{
			httpTransport.call(PT_SOAP_ACTION_FOR_EDIT_PROJECT, envelope);
			response = envelope.getResponse();
		}
		catch (Exception exception)
		{
			response=exception.toString();
		}
			return response.toString();
	}

public String GetProjects(String email_id)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_GET_PROJECT);
	PropertyInfo pi=new PropertyInfo();
		pi.setName("user_id");
	    pi.setValue(email_id);
	    pi.setType(String.class);
	    request.addProperty(pi);
	
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response=null;
		
		try
		{
			httpTransport.call(PT_SOAP_ACTION_FOR_GET_PROJECT, envelope);
			response = envelope.getResponse();
		}
		catch (Exception exception)
		{
			response=exception.toString();
		}
			return response.toString();
	}

public String GetProjectTypes()
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_GET_PROJECT_TYPE);
	    
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		Object response=null;
		
		try
		{
			httpTransport.call(PT_SOAP_ACTION_FOR_GET_PROJECT_TYPE, envelope);
			response = envelope.getResponse();
		}
		catch (Exception exception)
		{
			response=exception.toString();
		}
			return response.toString();
	}

public String GetCompany()
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_GET_COMPANY_LIST);
    
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet = true;
	
	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	
	try
	{
		httpTransport.call(PT_SOAP_ACTION_FOR_GET_COMPANY_LIST, envelope);
		response = envelope.getResponse();
	}
	catch (Exception exception)
	{
		response=exception.toString();
	}
		return response.toString();
	}

public String GetCompanyClients(String company_id)
{
	SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PT_OPERATION_NAME_FOR_GET_CLIENT_LIST);
    
	PropertyInfo pi=new PropertyInfo();
	pi.setName("companyId");
	pi.setValue(company_id);
	pi.setType(String.class);
	request.addProperty(pi);
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.dotNet = true;
	
	envelope.setOutputSoapObject(request);

	HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
	Object response=null;
	
	try
	{
		httpTransport.call(PT_SOAP_ACTION_FOR_GET_COMPANY_CLIENT_LIST, envelope);
		response = envelope.getResponse();
	}
	catch (Exception exception)
	{
		response=exception.toString();
	}
		return response.toString();
	}
}


