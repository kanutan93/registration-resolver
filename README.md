# registration-resolver

### Задача

Форма регистрации с отправкой имейла после одобрения из внешней системы.


Дана форма регистрации в нашем приложении, в которой необходимо заполнить:
- логин,
- пароль,
- адрес электронной почты,
- ФИО.


После отправки формы, мы регистрируем данные из нее в нашей БД, а также отправляем ее для одобрения во внешней системе. Пусть обмен с этой внешней системой будет через некое messaging решение. После одобрения или отклонения заявки, наше приложение должно отправить сообщение на электронную почту нашему пользователю с результатом проверки.


Стэк: JavaSE 8+, Spring boot 2, dbms - h2, Junit/Mockito/Assertj

### Запуск приложения: 
Для запуска приложения необходимы следующие зависимости:

- Git
- JDK 8
- Apache Maven 3.1.1+
- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

```shell
mvn clean install
docker-compose build --no-cache
docker-compose up
```

### Архитектура приложения:
![arch2 (1)](https://user-images.githubusercontent.com/11816371/117039475-7d1c7b80-ad11-11eb-9b4c-5e2e4bcaec44.png)

### Описание
1. Запускается [config - Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config/reference/html/) для централизованного хранения конфигурации. Все микросервисы: registration-service, notification-service, resolver-service, gateway, registry будут брать свой конфиг отсюда.
2. Запускается [registry - Spring Netflix Eureka](https://spring.io/projects/spring-cloud-netflix) - service discovery. Здесь регистрируются микросервисы: 
registration-service, notification-service, resolver-service, gateway
3. Запускается [gateway - Spring Netflix Zuul](https://spring.io/projects/spring-cloud-netflix) - gateway. В текущем проекте аналог reverse proxy. Проксирует запрос на микросервис registration-service.
4. Запускается в качестве нашего приложения 2 микросервиса: registration-service, notification-service. Для registration-service поднимается in-memory база h2.
5. Запускается в качестве внешнего  приложения resolver-service
6. Запускается RabbitMQ
7. Клиент выполняет запрос для регистрации в системе по описанной в задаче модели. Запрос должен отправляется в Gateway (Netflix Zuul), работающем на порту 8081. 
Пример:
```
curl --location --request POST 'http://localhost:8081' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "test",
    "password" : "123",
    "email": "test@gmail.com",
    "fullname": "TestFullname"
}'
```
8. Gateway адресует запрос на registration-service, работающем на порту 8082. Здесть переданные данные сохраняются в H2 и отправляются в очередь RabbitMQ registration->resolver.
9. resolver-service, в текущем проекте представляет внешнее приложение которое слушает сообщения из очереди registration->resolver. При получении сообщения из очереди, отрабатывает логика по принятию решения о статусе заявки (результат заявки генерируется рандомно) и полученные данные отправляются в очередь resolver->notification
10. notification-service cлушает сообщения из очереди resolver->notification и отправляет email сообщение на указанный в заявке email адрес(Заглушка в виде лога)

