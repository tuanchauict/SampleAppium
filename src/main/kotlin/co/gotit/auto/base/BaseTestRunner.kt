package co.gotit.auto.base

import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.service.local.AppiumDriverLocalService
import io.appium.java_client.service.local.AppiumServiceBuilder
import org.openqa.selenium.Platform
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import java.net.URL
import java.util.concurrent.TimeUnit


sealed class By {
    abstract fun get(): org.openqa.selenium.By
}

data class ById(val id: String) : By() {
    override fun get() = org.openqa.selenium.By.id(id)
}

data class ByXPath(val xpath: String) : By() {
    override fun get() = org.openqa.selenium.By.xpath(xpath)
}

data class ByText(val text: String) : By() {
    override fun get() = org.openqa.selenium.By.xpath("//*[@text=text]")
}

class BaseTestRunner(serverPort: Int, capabilities: DesiredCapabilities) {
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

        fun stopAppServer(port: Int) {
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


    fun findElement(by: By): WebElement? {
        return driver.findElement(by.get())
    }

    fun findElements(by: By): List<out WebElement>? {
        return driver.findElements(by.get())
    }

    fun fluentWaitFor(by: By, timeOutSecond: Long = 60L, ignoring: Array<Class<Throwable>> = emptyArray()) {
        val wait = FluentWait(driver)
                .withTimeout(timeOutSecond, TimeUnit.SECONDS)
                .pollingEvery(250L, TimeUnit.SECONDS)

        for (t in ignoring){
            wait.ignoring(t)
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(by.get()))
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