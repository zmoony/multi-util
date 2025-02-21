package com.boot.util.http;

import reactor.core.publisher.Mono;

/**
 * WebClientCutil
 *  <pre>
 *      WebClient 优于 RestTemplate 的原因有几个：非阻塞 I/O：WebClient 构建在 Reactor 之上，它提供了一种非阻塞、反应式的方法来处理 I/O。
 *      这可以在高流量应用程序中实现更好的可扩展性和更高的性能。函数式风格：WebClient 使用函数式编程风格，可以使代码更易于阅读和理解。
 *      它还提供了流畅的 API，可以更轻松地配置和自定义请求。更好地支持流式传输：WebClient 支持请求和响应正文的流式传输，这对于处理大文件或实时数据非常有用。
 *      改进的错误处理：WebClient 提供比 RestTemplate 更好的错误处理和日志记录，从而更轻松地诊断和解决问题。
 *  </pre>
 *
 *  <pre>
 *  创建代码：
 *      <code>
 *          HttpClient httpClient  =
 *                 HttpClient.create()
 *                         .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,  connectionTimeout)
 *                         .responseTimeout(Duration.ofMillis(requestTimeout))
 *                         .doOnConnected(conn  ->  conn.addHandlerLast(new ReadTimeoutHandler(readTimeout)));
 *
 *       WebClient client  =
 *                 WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
 *      </code>
 *  </pre>
 *
 *  <pre>
 *      同步请求：
 *      <code>
 *          public String postSynchronously(String url,  String requestBody)  {
 *     LOG.info("Going to hit API  -  URL  {}  Body  {}",  url,  requestBody);
 *     String response  =  "";
 *     try  {
 *         response  =
 *                 client
 *                         .method(HttpMethod.POST)
 *                         .uri(url)
 *                         .accept(MediaType.ALL)
 *                         .contentType(MediaType.APPLICATION_JSON)
 *                         .bodyValue(requestBody)
 *                         .retrieve()
 *                         .bodyToMono(String.class)
 *                         .block();
 *
 *     }  catch  (Exception ex)  {
 *         LOG.error("Error while calling API  ",  ex);
 *         throw new RunTimeException("XYZ service api error:  "  +  ex.getMessage());
 *     }  finally  {
 *         LOG.info("API Response  {}",  response);
 *     }
 *
 *     return  response;
 * }
 *      </code>
 *
 *  </pre>
 *
 *  <pre>
 *      异步请求：
 *      <code>
 *  public static Mono<String>  makePostRequestAsync(String url,  String postData)  {
 *         WebClient webClient  =  WebClient.builder().build();
 *         return  webClient.post()
 *                         .uri(url)
 *                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
 *                         .body(BodyInserters.fromFormData("data",  postData))
 *                         .retrieve()
 *                         .bodyToMono(String.class);
 * }
 *
 *      </code>
 *
 *  </pre>
 *
 *  <pre>
 *      处理异常：
 *      <code>
 *           public static Mono<String> makePostRequestAsync(String url, String postData) {
 *              WebClient webClient  =  WebClient.builder()
 *                         .baseUrl(url)
 *                         .build();
 *         return  webClient.post()
 *                         .uri("/")
 *                         .contentType(MediaType.APPLICATION_FORM_URLENCODED)
 *                         .body(BodyInserters.fromFormData("data",  postData))
 *                         .retrieve()
 *                         .onStatus(HttpStatus::is4xxClientError,  clientResponse  ->  Mono.error(new RuntimeException("Client error")))
 *                         .onStatus(HttpStatus::is5xxServerError,  clientResponse  ->  Mono.error(new RuntimeException("Server error")))
 *                         .bodyToMono(String.class);
 *
 *         }
 *      </code>
 *      针对异常处理的行为
 *      <code>
 *          makePostRequestAsync("https://example.com/api",  "param1=value1&param2=value2")
 *              .subscribe(response  ->  {
 *                      //  handle the response
 *                      System.out.println(response);
 *              },  error  ->  {
 *                                   //  handle the error
 *                                   System.err.println("An error occurred:  "  +  error.getMessage());
 *                                   if  (error instanceof WebClientResponseException)  {
 *                                           WebClientResponseException webClientResponseException  =  (WebClientResponseException)  error;
 *                                           int statusCode  =  webClientResponseException.getStatusCode().value();
 *                                           String statusText  =  webClientResponseException.getStatusText();
 *                                           System.err.println("Error status code:  "  +  statusCode);
 *                                           System.err.println("Error status text:  "  +  statusText);
 *                                   }
 * });
 *      </code>
 *
 *
 *  </pre>
 *
 *
 * @author yuez
 * @since 2024/11/6
 */
public class WebClientUtil {

}


















































