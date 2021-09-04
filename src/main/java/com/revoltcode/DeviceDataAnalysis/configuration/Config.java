package com.revoltcode.DeviceDataAnalysis.configuration;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Configuration
@EnableScheduling
@EnableElasticsearchRepositories(basePackages = "com.revoltcode.DeviceDataAnalysis.repository")
@ComponentScan(basePackages = "com.revoltcode.DeviceDataAnalysis")
public class Config extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String elasticSearchUrl;

    /*@Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration config = ClientConfiguration
                .builder()
                .connectedTo(elasticSearchUrl)
                .build();

        return RestClients.create(config).rest();
    }*/

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient(){
        RestClientBuilder restClientBuilder = null;

        try{
            var host = "localhost";
            var port = 9200;
            var user = "admin";
            var password = "admin";

            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

            SSLContext context = null;

            //CREATE A KEYSTORE OF TYPE "pkcs12"
            KeyStore keyStore = KeyStore.getInstance("pkcs12");


            /*
             * LOAD THE STORE
             * The first time you're doing this (i.e. the keystore does not
             * yet exist - you're creating it), you HAVE to load the keystore
             * from a null source with null password. Before any methods can
             * be called on your keystore you HAVE to load it first. Loading
             * it from a null source and null password simply creates an empty
             * keystore. At a later time, when you want to verify the keystore
             * or get certificates (or whatever) you can load it from the
             * file with your password.
             */
            keyStore.load(null, null);

            //GET FILE CONTAINING THE CERTIFICATE
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:certificate/elasticsearch-ca.crt");
            BufferedInputStream bis  = new BufferedInputStream((resource.getInputStream()));

            //.crt is an extension of X.509 type
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(bis);

            //ADD TO THE KEYSTORE AND GIVE IT AN ALIAS NAME
            keyStore.setCertificateEntry("", certificate);

            //INIT A TRUST MANAGER FACTORY
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //INIT A SSLCONTEXT
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagerFactory.getTrustManagers(), null);

            //BUILD A REST CLIENT WITH THE CREDENTIALS PROVIDER AND THE SSL CONTEXT INITIALIZED PREVIOUSLY
            SSLContext finalContext = context;
            restClientBuilder = RestClient.builder(
                    new HttpHost(host, port, "https"))
                    .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        httpAsyncClientBuilder.setSSLContext(finalContext);

                        return httpAsyncClientBuilder;
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return new RestHighLevelClient(restClientBuilder);
    }
}


























