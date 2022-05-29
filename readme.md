Сначала сделал простые модели, немного кривые связи. 
Все данные хранились в H2. 
Методы апи для получения авторов и популяции базы.
Добавил тестов

Добавил использование "нормальной" базы, а не H2. База запускается внутри контейнера:  
`docker run --name demo-db -p 3307:3306 -e MYSQL_ROOT_PASSWORD=1 -d mysql`
После запуска контейнера надо !ПОДОЖДАТЬ! потом залогиниться:  
`mysql --host=localhost --port 3307 -u root -p`
Создать БД и пользователя, который указан в _application.properties_:  
`create database demo;`  
`create user 'demo'@'%' identified by '1';`  
`grant all on demo.* to 'demo'@'%';`
