package ee.potatonet;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import ee.potatonet.messaging.AuthenticationPrincipalArgumentResolver;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigration extends AbstractWebSocketMessageBrokerConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/feed", "/topic");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
    stompEndpointRegistry.addEndpoint("/feed-ws").withSockJS();
  }
}
