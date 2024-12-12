package vttp.batch5.ssf.revision;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// This class is marked as a configuration class that will provide beans to the Spring context
@Configuration
public class AppConfig {

   // Logger instance to log any necessary information in the configuration
   private final Logger logger = Logger.getLogger(AppConfig.class.getName());

   // Inject Redis configurations from the application.properties file into this class
   @Value("${spring.data.redis.host}")
   private String redisHost;

   @Value("${spring.data.redis.port}")
   private int redisPort;

   @Value("${spring.data.redis.database}")
   private int redisDatabse;

   @Value("${spring.data.redis.username}")
   private String redisUsername;

   @Value("${spring.data.redis.password}")
   private String redisPassword;

   // Defines a bean to create a RedisTemplate that interacts with Redis
   @Bean("redis-0")
   public RedisTemplate<String, Object> createRedisTemplateObject() {
      
      // Create a RedisStandaloneConfiguration object with the host and port for the Redis server
      RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
      // Set the Redis database index (e.g., select database 0)
      config.setDatabase(redisDatabse);
      
      // If a username and password are provided, set them in the configuration
      if (!redisUsername.trim().equals("")) {
         logger.info(">>>> Setting Redis username and password");
         config.setUsername(redisUsername);
         config.setPassword(redisPassword);
      }

      // Create a client configuration using the Jedis client
      JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

      // Create a JedisConnectionFactory which will use the Redis configuration and client
      JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
      // Ensure all properties are set in the connection factory
      jedisFac.afterPropertiesSet();

      // Create a RedisTemplate to interact with Redis
      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
      // Set the connection factory for the RedisTemplate
      redisTemplate.setConnectionFactory(jedisFac);
      // Use a StringRedisSerializer for key serialization
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      // Use a StringRedisSerializer for hash key serialization
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      
      // Return the configured RedisTemplate
      return redisTemplate;
   }
}

// 2) Big Idea Behind the Code:
// This configuration class defines a RedisTemplate bean, which is essential for interacting with a Redis data store in a Spring application. Here's a breakdown of the main objectives:

// Configuration Injection: The Redis connection details (host, port, database index, username, password) are injected from the application.properties (or equivalent configuration file).
// RedisStandaloneConfiguration: This class is used to configure the Redis connection settings, such as the host, port, database number, and credentials if provided.
// JedisClientConfiguration: This configures the Jedis client (which is a Java Redis client) that will be used to connect to the Redis server.
// RedisTemplate: This class is the key to interacting with Redis in a Spring application. It supports operations like storing and retrieving data from Redis. It is configured with serializers for keys and values, which ensure that Redis handles data correctly.
// By creating and configuring this RedisTemplate bean, the application can easily use Redis to store and retrieve data using Spring Data Redis.

// 3) Can This Code Always Be the Same?
// Yes, with some assumptions: The code can remain the same as long as:
// The Redis connection settings (host, port, database, username, password) are consistent across environments.
// The application is using Jedis as the Redis client. If a different client (e.g., Lettuce) is used in the future, the code would need to be adjusted accordingly.
// The application needs a single Redis connection with the specified configuration. If you need to support multiple Redis connections or databases, you may need to create additional RedisTemplate beans with different configurations.