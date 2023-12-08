create table if not exists `author`
(
    `id`            int unsigned not null auto_increment,
    `given_name`    varchar(32)  not null,
    `surname`       varchar(32) default null,
    `date_of_birth` date        default null,
    `date_of_death` date        default null,
    `created_at`    datetime(0) default now(0),
    `updated_at`    datetime(0) default now(0) on update now(0),
    primary key (`id`),
    unique (`given_name`, `surname`)
);
create table if not exists `genre`
(
    `id`         int unsigned not null auto_increment,
    `name`       varchar(32)  not null,
    `created_at` datetime(0) default now(0),
    `updated_at` datetime(0) default now(0) on update now(0),
    primary key (`id`),
    unique (`name`)
);
create table if not exists `book`
(
    `id`         int unsigned not null auto_increment,
    `title`      varchar(64)  not null,
    `summary`    text         default null,
    `isbn`       varchar(13)  not null,
    `created_at` datetime(0)  default now(0),
    `updated_at` datetime(0)  default now(0) on update now(0),
    `author_id`  int unsigned not null,
    `genre_id`   int unsigned default null,
    primary key (`id`),
    unique (`title`),
    unique (`isbn`),
    foreign key (`author_id`) references `author` (`id`),
    foreign key (`genre_id`) references `genre` (`id`)
);
create table if not exists `book_instance`
(
    `id`         int unsigned not null auto_increment,
    `imprint`    varchar(64)  not null,
    `status`     varchar(32) comment 'enum AVAILABLE, ON_HOLD, ON_LOAN, OUT_OF_CIRCULATION',
    `created_at` datetime(0) default now(0),
    `updated_at` datetime(0) default now(0) on update now(0),
    `book_id`    int unsigned not null,
    primary key (`id`),
    foreign key (`book_id`) references `book` (`id`)
);