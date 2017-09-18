package co.gotit.auto.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseTestRunner {
    private static final Map<Integer, AppiumDriverLocalService> APPIUM_SERVERS = new HashMap<>();

    private int mServerPort;

    AndroidDriver mDriver;
    AppiumDriverLocalService mAppiumServer;



    public BaseTestRunner(int serverPort, DesiredCapabilities capabilities) throws MalformedURLException {
        mServerPort = serverPort;

        mAppiumServer = createAppServer(serverPort);

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "7.0");
        capabilities.setCapability("deviceName", "A7.0");
        AndroidDriver driver = new AndroidDriver(new URL(String.format("http://0.0.0.0:%d/wd/hub", mServerPort)), capabilities);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        mDriver = driver;
    }

    private AppiumDriverLocalService createAppServer(int port){
        if(APPIUM_SERVERS.containsKey(port))
            return APPIUM_SERVERS.get(port);

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingPort(port);
        AppiumDriverLocalService server = AppiumDriverLocalService.buildService(builder);
        server.start();

        return server;
    }


    public AndroidDriver getDriver() {
        return mDriver;
    }

    public int getServerPort() {
        return mServerPort;
    }

    public void stopDriver(){
        mDriver.closeApp();
    }

    public void stopServer(){
        mAppiumServer.stop();
        APPIUM_SERVERS.remove(mServerPort);
    }

    public static class CapabilitiesBuilder {
        private DesiredCapabilities mCapabilities = new DesiredCapabilities();

        public CapabilitiesBuilder setCapability(String capabilityName, String value){
            mCapabilities.setCapability(capabilityName, value);
            return this;
        }
        public CapabilitiesBuilder setCapability(String capabilityName, boolean value){
            mCapabilities.setCapability(capabilityName, value);
            return this;
        }
        public CapabilitiesBuilder setCapability(String capabilityName, Platform value){
            mCapabilities.setCapability(capabilityName, value);
            return this;
        }
        public CapabilitiesBuilder setCapability(String key, Object value){
            mCapabilities.setCapability(key, value);
            return this;
        }

        public DesiredCapabilities build(){
            return mCapabilities;
        }
    }
}
