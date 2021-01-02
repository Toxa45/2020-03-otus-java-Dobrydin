package ru.otus;

import com.google.gson.Gson;
import ru.otus.gsom.MyGson;
import ru.otus.model.AnyObject;

public class SerializationDemo {

  public static void main(String[] args) {
    AnyObject anyObject = null;
    Gson gson = new Gson();
    System.out.println(gson.toJson(anyObject));

    MyGson myGson = new MyGson();
    String myJson = myGson.toJson(anyObject);
    System.out.println(myJson);
    AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
    System.out.println(anyObject == obj2);


    anyObject = new AnyObject(1, 1.5, 2, 2.5, "HAPPY NEW OBJECT");

    System.out.println(gson.toJson(anyObject));
    myJson = myGson.toJson(anyObject);
    System.out.println(myJson);
    obj2 = gson.fromJson(myJson, AnyObject.class);
    System.out.println(anyObject.equals(obj2));

  }
}
