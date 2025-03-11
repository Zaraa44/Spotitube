/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2014                    */
/* Created on:     11-3-2025 15:42:27                           */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('PLAYLIST') and o.name = 'FK_PLAYLIST_USERPLAYL_USER')
alter table PLAYLIST
   drop constraint FK_PLAYLIST_USERPLAYL_USER
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TRACKPLAYLIST') and o.name = 'FK_TRACKPLA_TRACKPLAY_PLAYLIST')
alter table TRACKPLAYLIST
   drop constraint FK_TRACKPLA_TRACKPLAY_PLAYLIST
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('TRACKPLAYLIST') and o.name = 'FK_TRACKPLA_TRACKPLAY_TRACK')
alter table TRACKPLAYLIST
   drop constraint FK_TRACKPLA_TRACKPLAY_TRACK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('PLAYLIST')
            and   name  = 'USERPLAYLIST_FK'
            and   indid > 0
            and   indid < 255)
   drop index PLAYLIST.USERPLAYLIST_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('PLAYLIST')
            and   type = 'U')
   drop table PLAYLIST
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TRACK')
            and   type = 'U')
   drop table TRACK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('TRACKPLAYLIST')
            and   name  = 'TRACKPLAYLIST2_FK'
            and   indid > 0
            and   indid < 255)
   drop index TRACKPLAYLIST.TRACKPLAYLIST2_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('TRACKPLAYLIST')
            and   name  = 'TRACKPLAYLIST_FK'
            and   indid > 0
            and   indid < 255)
   drop index TRACKPLAYLIST.TRACKPLAYLIST_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TRACKPLAYLIST')
            and   type = 'U')
   drop table TRACKPLAYLIST
go

if exists (select 1
            from  sysobjects
           where  id = object_id('"USER"')
            and   type = 'U')
   drop table "USER"
go

/*==============================================================*/
/* Table: PLAYLIST                                              */
/*==============================================================*/
create table PLAYLIST (
   PLAYLISTID           char(10)             not null,
   TOKEN                int                  not null,
   NAME                 char(10)             not null,
   LENGTH               char(10)             null,
   constraint PK_PLAYLIST primary key (PLAYLISTID)
)
go

/*==============================================================*/
/* Index: USERPLAYLIST_FK                                       */
/*==============================================================*/




create nonclustered index USERPLAYLIST_FK on PLAYLIST (TOKEN ASC)
go

/*==============================================================*/
/* Table: TRACK                                                 */
/*==============================================================*/
create table TRACK (
   TRACKID              int                  not null,
   TITLE                char(255)            not null,
   PERFORMER            char(255)            not null,
   DURATION             int                  not null,
   ALBUM                char(255)            not null,
   PLAYCOUNT            int                  null,
   PUBLICATIONDATE      datetime             not null,
   DESCRIPTION          char(255)            not null,
   OFFLINEAVAILABLE     bit                  null,
   constraint PK_TRACK primary key (TRACKID)
)
go

/*==============================================================*/
/* Table: TRACKPLAYLIST                                         */
/*==============================================================*/
create table TRACKPLAYLIST (
   PLAYLISTID           char(10)             not null,
   TRACKID              int                  not null,
   constraint PK_TRACKPLAYLIST primary key (PLAYLISTID, TRACKID)
)
go

/*==============================================================*/
/* Index: TRACKPLAYLIST_FK                                      */
/*==============================================================*/




create nonclustered index TRACKPLAYLIST_FK on TRACKPLAYLIST (PLAYLISTID ASC)
go

/*==============================================================*/
/* Index: TRACKPLAYLIST2_FK                                     */
/*==============================================================*/




create nonclustered index TRACKPLAYLIST2_FK on TRACKPLAYLIST (TRACKID ASC)
go

/*==============================================================*/
/* Table: "USER"                                                */
/*==============================================================*/
create table "USER" (
   USERNAME             char(255)            not null,
   PASSWORD             char(255)            not null,
   TOKEN                int                  not null,
   constraint PK_USER primary key (TOKEN)
)
go

alter table PLAYLIST
   add constraint FK_PLAYLIST_USERPLAYL_USER foreign key (TOKEN)
      references "USER" (TOKEN)
go

alter table TRACKPLAYLIST
   add constraint FK_TRACKPLA_TRACKPLAY_PLAYLIST foreign key (PLAYLISTID)
      references PLAYLIST (PLAYLISTID)
go

alter table TRACKPLAYLIST
   add constraint FK_TRACKPLA_TRACKPLAY_TRACK foreign key (TRACKID)
      references TRACK (TRACKID)
go

