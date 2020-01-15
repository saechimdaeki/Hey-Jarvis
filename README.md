# Hey-Jarvis
 An Android application that performs commands with voice recognition
 - https://developer.android.com/guide/components/intents-common.html#java
 

### Develop progress
#### [개발진행상황](./DevelopNote)


## 🤝 How to Contribute
1.  Fork it
2.  Create your feature branch (git checkout -b my-new-feature)
3.  Commit your changes (git commit -am 'Add some feature')
4.  Push to the branch (git push origin my-new-feature)
5.  Create new Pull Request


## 👀 Todo List
 * Parsing with Jsoup
 * Alarms Alignment Function
 * dialing function
 * camera function 
* 택시 불러 줘 라고말할시 
   callCar() 호출  (This function is only available on wear os..?)
<code>

      public void callCar() {
 
         Intent intent = new Intent(ReserveIntents.ACTION_RESERVE_TAXI_RESERVATION);
          
          if (intent.resolveActivity(getPackageManager()) != null) {
           
           startActivity(intent);
        
        }
      }
</code>

