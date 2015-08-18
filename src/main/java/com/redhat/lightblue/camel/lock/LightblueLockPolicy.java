package com.redhat.lightblue.camel.lock;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.ExpressionNodeHelper;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.model.language.ExpressionDefinition;
import org.apache.camel.spi.Policy;
import org.apache.camel.spi.RouteContext;

import com.redhat.lightblue.client.Locking;

/**
 * Creates a lock (aka. acquire) in lightblue for each Exchange and then unlocks (aka. release) when finished.
 * If a lock cannot be acquired, then {@link Processor} will be skipped over.
 *
 * @author dcrissman
 */
public class LightblueLockPolicy implements Policy {

    public final static String LOCK_RESOURCE_ID = "LOCK_RESOURCE_ID";

    private final Expression lockExpression;
    private final Expression resourceExpression;

    /**
     * @param lockExpression - {@link Expression} to obtain a Lightblue {@link Locking} instance.
     * @param resourceExpression - {@link Expression} for determining the resourceId for a given Exchange.
     */
    public LightblueLockPolicy(Expression lockExpression, Expression resourceExpression) {
        this.lockExpression = lockExpression;
        this.resourceExpression = resourceExpression;
    }

    @Override
    public void beforeWrap(RouteContext routeContext, ProcessorDefinition<?> definition) {
        //Do Nothing!!
    }

    @Override
    public Processor wrap(final RouteContext routeContext, final Processor processor) {
        final Expression routeLockExpression = createRouteExpression(routeContext, lockExpression);
        final Expression routeResourceExpression = createRouteExpression(routeContext, resourceExpression);

        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                final Locking lock = routeLockExpression.evaluate(exchange, Locking.class);
                final String resourceId = routeResourceExpression.evaluate(exchange, String.class);

                //TODO NPEs

                routeContext.getRoute().setHeader(LOCK_RESOURCE_ID, new ConstantExpression(resourceId));

                if (lock.acquire(resourceId)) {
                    processor.process(exchange);
                    lock.release(resourceId);
                }
                else{
                    routeContext.getRoute().stop();
                }

                routeContext.getRoute().removeHeader(LOCK_RESOURCE_ID);
            }
        };

    }

    private Expression createRouteExpression(final RouteContext routeContext, final Expression expression){
        ExpressionDefinition red = ExpressionNodeHelper.toExpressionDefinition(expression);
        return red.createExpression(routeContext);
    }

}