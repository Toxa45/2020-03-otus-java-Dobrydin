package ru.otus.gsom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.AnyObject;

@DisplayName("Сириализатор json MyGson")
class MyGsonTest {

  private Gson gson;
  private MyGson myGson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    myGson = new MyGson();
  }

  @Test
  @DisplayName("Сериализовать объект AnyObject, результат проверить gson")
  void toJson() {
    AnyObject anyObject = null;

    String myJson = myGson.toJson(anyObject);
    AnyObject objDes = gson.fromJson(myJson, AnyObject.class);
    assertEquals(anyObject, objDes, "ошибка при сериализации - "+anyObject);


    anyObject = new AnyObject(1, 1.5, 2, 2.5, "HAPPY NEW OBJECT");
    System.out.println(gson.toJson(anyObject));
    myJson = myGson.toJson(anyObject);
    objDes = gson.fromJson(myJson, AnyObject.class);
    assertEquals(anyObject, objDes, "ошибка при сериализации - "+anyObject);
  }
}
