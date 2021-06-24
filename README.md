<h1>Проект: Автоматизированная система учета и контроля исполнения договоров с клиентами торговой компании «Авангард»</h1>
<h2>Назначение проекта</h2>

Данная программа предназначена для автоматизации учета и контроля исполнения договоров с клиентами. Функции автоматизированной системы:

- прикрепление и добавление заключенных договоров;
- изменение статуса исполнения и даты исполнения заключенных договоров;
- поиск необходимых договоров;
- поиск необходимых договоров по определенному критерию;
- открытие и просмотр прикрепленного файла договора;
- удаление договоров;
- запрос подтверждения перед удалением;
- учет всех договоров;
- учет неисполненных договоров;
- учет исполненных договоров.

<h2> Минимальные системные требования </h2>

- процессор с тактовой частотой от 1ГГц;
- оперативная память не менее 256Мб;
- видеопамять не менее 256Мб;
- место на накопителе не менее 512 Мб;
- операционная система Windows XP и выше.

<h1>Руководство пользователя</h1>
<h2>Установка</h2>

-  перейти в [репозиторий](https://github.com/Vladislav-Kudrin/contractsAccounting);
-  нажать на кнопку "Code" и в выпадающем меню выбрать "Download ZIP";
-  распаковать скачанный архив;
-  импортировать дамп базы данных "contracts_accounting.sql" в папке "db";
-  указать хост, порт, пользователя и пароль к импортированной базе данных в файле "src/sample/DBConfig.java";
-  скомпилировать исходники в папке "src/sample/*".
-  запустить скомпилированный jar.

<h2>Работа с программой</h2>
<h3>Добавление договора</h3>

- заполнить необходимые поля;
- прикрепить файл договора;
- нажать кнопку "Добавить".

<h3>Изменение статуса исполнения</h3>

- выделить нужный договор в таблице;
- нажать кнопку "Изменить";
- ввести необходимые изменения;
- нажать кнопку "Подтвердить";

<h3>Открытие и просмотр файла договора</h3>

- выделить необходимый договор в таблице;
- нажать кнопку "Открыть".

<h3>Поиск договора</h3>

- выбрать критерий для поиска;
- ввести запрос для поиска;
- нажать кнопку "Поиск".

<h3>Удаление договора</h3>

- выделить нужный договор в таблице;
- ввести номер выделенного договора в поле для подтверждения удаления;
- нажать кнопку "Удалить".