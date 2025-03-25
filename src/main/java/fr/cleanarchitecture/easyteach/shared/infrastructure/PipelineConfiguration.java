package fr.cleanarchitecture.easyteach.shared.infrastructure;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {

    @Bean
    public Pipeline getPipeline(
            ObjectProvider<Command.Handler> commandHandler,
            ObjectProvider<Command.Middleware> commandMiddleware,
            ObjectProvider<Notification.Handler> notificationHandles
    ) {
        return new Pipelinr()
                .with(commandHandler::orderedStream)
                .with(commandMiddleware::orderedStream)
                .with(notificationHandles::orderedStream);
    }
}
