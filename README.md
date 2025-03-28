# Описание проекта
Разработан микросервис на Spring Boot, который
1. Генерирует CDR записи (создание случайных записей о звонках за последний год для всех абонентов)
2. Генерирует CDR отчет (формирование отчета по звонкам для конкретного абонента за указанный период времени)
3. Генерирует UDR отчет (формирование отчета по звонкам для конкретного абонента или всех абонентов за указанный месяц)
 # Шаги для запуска
 **1. Клонировать репозиторий**
 ```sh git clone https://github.com/Sonchello/test_nexign_boostrap.git
cd test_nexign_boostrap
 ```

**2. Собрать и запустить**
 ```sh git clone https://github.com/Sonchello/test_nexign_boostrap.git
./mvnw spring-boot:run
 ```
 # Настройка H2 Database
В проекте используется H2 Database, которая является встроенной in-memory базой данных. База данных создается и управляется автоматически при запуске приложения. 
Для проверки подключения базы данных выполнен перехрд по адресу 

 ```sh git clone https://github.com/Sonchello/test_nexign_boostrap.git
http://localhost:8080/h2-console
 ```
введенны данные для подключения

<p align="center">
  <img src="https://github.com/user-attachments/assets/3cae33c2-ad7f-42af-9edb-ceb8f639615a">
</p>

таблица  Abonent заполняется данными с номерами телефонов абонентов    

<p align="center">
  <img  src="https://github.com/user-attachments/assets/a5868ac9-9d8b-4f8f-99c9-cc4bd7f752a4">
</p>


 # Описание эндпоинтов API и тестирование в Postman
## Генерация CDR записей
  ```sh git clone https://github.com/Sonchello/test_nexign_boostrap.git
http://localhost:8080/cdr/generate
 ```
 генерирует CDR записи для всех абонентов за последний год.Для каждого абонента случайным образом определяется количество звонков за день. Для каждого звонка случайным образом выбирается получатель (исключая вызов самому себе). Записи сохраняются в базе данных 
 
 <p align="center">
  <img  src="https://github.com/user-attachments/assets/35b22a3d-7b7c-454f-a5d4-8446f7dee278">
</p>

Сгенерированные записи
 <p align="center">
  <img src="https://github.com/user-attachments/assets/037ed866-d893-44b1-9c1a-d7a785662c40">
</p>

## Формирование UDR отчета

UDR отчет предоставляет информацию о звонках для конкретного абонента или всех абонентов за указанный месяц. Отчет предоставляет общую продолжительность входящих звонков и общую продолжительность исходящих звонков.
Выполнен запрос для получения UDR отчета для конкретного абонента

<p align="center">
  <img src="https://github.com/user-attachments/assets/df9fd00c-4441-4734-ba2b-49c030388ae4">
</p>
Выполнение запроса для получения UDR отчета для всех абонентов

<p align="center">
  <img src="https://github.com/user-attachments/assets/5e13ec49-c326-458f-a043-c327bc366504">
</p>

## Формирование CDR отчета
Отчет, сгенерированный через запрос 
  ```sh git clone https://github.com/Sonchello/test_nexign_boostrap.git
http://localhost:8080/cdr/report
 ```
содержит информацию о звонках для указанного абонента (msisdn) за указанный период времени (startDate — endDate)
 <p align="center">
  <img src="https://github.com/user-attachments/assets/48ceb87c-7dfb-4ff5-9899-30ba0699ba3f">
</p>
Сгенерированный отчет сохраняется в файл на сервере, в директорию reports в виде CSV-файла. Название файла содержит номер пользователя и уникальный UUID запроса
 <p align="center">
  <img src="https://github.com/user-attachments/assets/3d4294c0-cd6d-4dc0-a6bd-b14cc436b5fc">
</p>

Содержимое отчета
 <p align="center">
  <img src="https://github.com/user-attachments/assets/7e896861-36a3-46b6-acf9-d5ca26282b73">
</p>

