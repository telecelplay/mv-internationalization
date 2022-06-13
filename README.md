we need an easy way to localize messages produced in scripts, and labels for entities like CET, CFT, endpoints for this purpose this module is introduced


Usage examples: 


** Example 1

Let asssume that there is no LocalizedMessage in DB
```
public class HelloWorldScript extend EndpointScript {

    String helloWorldMessage;
    String welcomeMessage;

    public void execute(Map<String,Object> context){
       Internationalizer i18n = Internationalizer.getInstance(this,context);
       helloWorldMessage= i18n.get("my.message.key","hello world");
       welcomeMessage=i18n.get("my.secondmessage.key");
    }
}
```

helloWorldMessage will be `hello world`(MessageLocalizer create the missing LocalizedMessage);
welcomeMessage will be `my.secondmessage.key`


** Example 2

Let asssume that there is 2 LocalizedMessage in DB:
| module | key | language | value |
|--------|-----|----------|-------|
| `mymodule` | `my.message.key` | `en` | `I am ${age} year${age>1?'':'s'} old` |
| `mymodule` | `my.message.key` | `fr` | `J'ai ${age} an${age>1?'':'s'}` |

```
public class HelloWorldScript extend EndpointScript {

    String helloWorldMessage1;
    String helloWorldMessage2;
    String helloWorldMessage3;

    public void execute(Map<String,Object> context){
       Internationalizer i18n = Internationalizer.getInstance(this,context);
       helloWorldMessage1= i18n.get("my.message.key");
       helloWorldMessage2= i18n.get("my.message.key", Map.of({"age",1}););
       helloWorldMessage2= i18n.get("my.message.key", Map.of({"age",20}););
    }
}
```

Assume that we call the Script from an endpoint and that the http request contains the heade "Accepted-Language : Fr-fr"

helloWorldMessage1 will be `J'ai ${age} an${age>1?'':'s'}`

helloWorldMessage2 will be `J'ai 1 an`

helloWorldMessage3 will be `J'ai 20 ans`
