@Grab('org.apache.camel:camel-core:2.22.0')
@Grab('org.apache.camel:camel-quartz2:2.22.0')
@Grab('org.apache.camel:camel-telegram:2.22.0')
@Grab('org.slf4j:slf4j-simple:1.7.25')

import org.apache.camel.*
import org.apache.camel.impl.*
import org.apache.camel.builder.*
import org.apache.camel.component.slack.*
import org.apache.camel.util.jndi.JndiContext
import org.apache.camel.routepolicy.quartz2.CronScheduledRoutePolicy

def camelContext = new DefaultCamelContext()
def gpioIrrigation = '/sys/class/gpio/gpio16/value'

'echo 16 > /sys/class/gpio/export'.execute()
'echo "out" > /sys/class/gpio/gpio16/direction'.execute()
//0+*+*+?+*+* every min

camelContext.addRoutes(new RouteBuilder() {
    def void configure() {
	getContext().getGlobalOptions().put(Exchange.LOG_EIP_NAME, 'irrigation');
        from('quartz2://quartz2Test?cron=0+0+*/3+?+*+*').routeId("irrigation")
        .log('Irrigation started')
      	.transform()
      	.simple('1')
	.to("file:?fileName=$gpioIrrigation&autoCreate=true")	
        .delay(6000)
	.transform()
      	.simple('0')
	.to("file:?fileName=$gpioIrrigation&autoCreate=true")
	.transform()
	.simple('Irrigation done')
	.to("telegram:bots/")
	.log('Irrigation done')
	.to("mock:success")
    }
})
camelContext.start()

addShutdownHook{ camelContext.stop() }
synchronized(this){ this.wait() }
