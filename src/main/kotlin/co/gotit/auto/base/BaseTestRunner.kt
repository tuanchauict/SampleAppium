package co.gotit.auto.base

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.service.local.AppiumDriverLocalService
import io.appium.java_client.service.local.AppiumServiceBuilder
import org.openqa.selenium.By
import org.openqa.selenium.Platform
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import java.net.URL
import java.util.concurrent.TimeUnit

class BaseTestRunner(serverPort: Int, capabilities: DesiredCapabilities){
    companion object {
        private val APPIUM_SERVERS = HashMap<Int, AppiumDriverLocalService>()

        fun getAppServer(port: Int): AppiumDriverLocalService {
            if (APPIUM_SERVERS.containsKey(port))
                return APPIUM_SERVERS[port]!!
            val builder = AppiumServiceBuilder()
                    .usingPort(port)
            val server = AppiumDriverLocalService.buildService(builder)
            server.start()
            APPIUM_SERVERS[port] = server
            return server
        }

        fun stopAppServer(port: Int){
            val server = APPIUM_SERVERS.remove(port)
            server?.stop()
        }
    }

    private val driver: AndroidDriver<*>

    init {
        getAppServer(serverPort)
        driver = AndroidDriver<WebElement>(URL("http://0.0.0.0:$serverPort/wd/hub"), capabilities)
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS)
    }


    fun findElementById(id: String): WebElement? {
        return driver.findElement(By.id(id))
    }

    fun findElementByXpath(xpath: String): WebElement? {
        return driver.findElement(By.xpath(xpath))
    }

    fun findElementByText(text: String): WebElement?{
        return driver.findElement(By.xpath("//*[@text='$text']")) //need test
    }

    fun findElementsById(id: String): List<out WebElement>? {
        return driver.findElements(By.id(id))
    }

    fun findElementsByXpath(xpath: String): List<out WebElement>?{
        return driver.findElements(By.xpath(xpath))
    }

    fun findElementsByText(text: String): List<out WebElement>?{
        return driver.findElements(By.xpath("//*[@text='$text']"))//need test
    }

    fun waitFor(){

    }
}

class CapabilitiesBuilder {
    private val mCapabilities = DesiredCapabilities()

    fun setCapability(capabilityName: String, value: String): CapabilitiesBuilder {
        mCapabilities.setCapability(capabilityName, value)
        return this
    }

    fun setCapability(capabilityName: String, value: Boolean): CapabilitiesBuilder {
        mCapabilities.setCapability(capabilityName, value)
        return this
    }

    fun setCapability(capabilityName: String, value: Platform): CapabilitiesBuilder {
        mCapabilities.setCapability(capabilityName, value)
        return this
    }

    fun setCapability(key: String, value: Any): CapabilitiesBuilder {
        mCapabilities.setCapability(key, value)
        return this
    }

    fun build(): DesiredCapabilities {
        return mCapabilities
    }
}