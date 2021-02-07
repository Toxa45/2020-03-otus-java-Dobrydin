package ru.otus;

/**
 * @author dobrydin
 * @since 07.02.2021
 */
public class MessageGenerateInt implements MessageGenerate {

  private int numberToPrint = 1;
  private int increment = 1;

  @Override
  public int messageNext(){
    int numberToPrintReturn = numberToPrint;
    numberToPrint +=increment;

    if(numberToPrint >=10)
      increment = -1;
    if(numberToPrint <=1)
      increment = 1;
    return numberToPrintReturn;
  }
}
