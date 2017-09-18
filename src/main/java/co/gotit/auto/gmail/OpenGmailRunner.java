package co.gotit.auto.gmail;

import co.gotit.auto.base.BaseTestRunner;
import io.appium.java_client.android.AndroidDriver;

import java.net.MalformedURLException;

public class OpenGmailRunner extends BaseTestRunner {


    public OpenGmailRunner() throws MalformedURLException {
        super(1235, new CapabilitiesBuilder()
                .setCapability("noReset", true)
                .setCapability("appPackage", "com.google.android.gm")
                .setCapability("appActivity", "ConversationListActivityGmail")
                .build());
    }

    public void clickLoginEmail(){
        String senderNameId = "com.google.android.gm:id/sender_name";
        String senderName = "Account Kit";

    }

    public static void main(String[] args) throws MalformedURLException {
        OpenGmailRunner gmailRunner = new OpenGmailRunner();
        AndroidDriver driver = gmailRunner.getDriver();

    }
}
