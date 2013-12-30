package sample;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import unit.RefObj;

import util.CommonUtil;

public class MethodSample implements IExec
{

	private List<Object> listInvokers = new ArrayList<Object>();
	private List<Method> listMethods = new ArrayList<Method>();
	private Map<Integer, Object> mapArgs = new HashMap<Integer, Object>();
	
	@Override
	public void Exec()
	{
		try
		{
			Method method1 = CommonUtil.getAccessibleMethod(MethodSample.class, "method1", Object[].class);
			Method method2 = CommonUtil.getAccessibleMethod(MethodSample.class, "method2", Object[].class);
			Method method3 = CommonUtil.getAccessibleMethod(MethodSample.class, "test", RefObj.class);
			Method method4 = CommonUtil.getAccessibleMethod(MethodSample.class, "test2", RefObj.class);
			
			Method method5 = CommonUtil.getAccessibleMethod(MethodSample.class, "test3", String.class);
			
			int nIndex = 0;
			this.listInvokers.add(this);
			this.listMethods.add(method1);
			this.mapArgs.put(nIndex, new Object[]{"123", 456});
			
			nIndex++;
			
			this.listInvokers.add(this);
			this.listMethods.add(method2);
			this.mapArgs.put(nIndex, new Object[]{111, "bank"});
			
			nIndex++;
			
//			this.listInvokers.add(this);
//			this.listMethods.add(method3);
//			this.mapArgs.put(nIndex, new Object[]{111, "bank"});
			
			this.invoke();
			
			int n = 222;
			RefObj refObj = new RefObj("bank2");
			String str = "bank2";
			test(refObj);
			test2(refObj);
			
			method3.invoke(this, new Object[]{ refObj });
			method4.invoke(this, new Object[]{ refObj });
			
			System.out.println(refObj.get());
			
			method5.invoke(this, (Object)str);
			//method5.invoke(this, 5);
			
		}
		catch(Exception exc)
		{
			System.out.println(exc.getMessage());
		}
	}
	
	public void method1(Object... objsArgs)
	{
		for(Object obj : objsArgs)
		{
			System.out.println(obj);
		}
	}
	
	public void method2(Object... objs)
	{
		int n = (Integer)objs[0];
		String str = (String)objs[1];
		
		System.out.println(n + str);
	}
	
	public void test(RefObj refObj)
	{
		System.out.println("test..." + refObj.get());
		refObj.set("test");
	}
	
	public void test2(RefObj refObj)
	{
		System.out.println("test2..." + refObj.get());
		refObj.set("test2");
	}
	
	public void test3(String str)
	{
		System.out.println("test3 String");
	}
	
	public void test3(int n)
	{
		System.out.println("test3 int");
	}
	
	public void invokeMethod1(Object... objs) throws Exception
	{
		
	}
	
	private void invoke() throws Exception
	{
		Method method;
		
		for(int i = 0; i < this.listMethods.size(); i++)
		{
			this.listMethods.get(i).invoke(this.listInvokers.get(i), this.mapArgs.get(i));
		}
	}

}
