/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2014                    */
/* Created on:     18-3-2025 09:55:40                           */
/*==============================================================*/


if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('PLAYLIST') and o.name = 'FK_PLAYLIST_USERPLAYL_USERS')
alter table PLAYLIST
   drop constraint FK_PLAYLIST_USERPLAYL_USERS
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('PLAYLISTTRACKS') and o.name = 'FK_PLAYLIST_PLAYLISTT_PLAYLIST')
alter table PLAYLISTTRACKS
   drop constraint FK_PLAYLIST_PLAYLISTT_PLAYLIST
go

if exists (select 1
   from sys.sysreferences r join sys.sysobjects o on (o.id = r.constid and o.type = 'F')
   where r.fkeyid = object_id('PLAYLISTTRACKS') and o.name = 'FK_PLAYLIST_PLAYLISTT_TRACKS')
alter table PLAYLISTTRACKS
   drop constraint FK_PLAYLIST_PLAYLISTT_TRACKS
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
            from  sysindexes
           where  id    = object_id('PLAYLISTTRACKS')
            and   name  = 'PLAYLISTTRACKS2_FK'
            and   indid > 0
            and   indid < 255)
   drop index PLAYLISTTRACKS.PLAYLISTTRACKS2_FK
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('PLAYLISTTRACKS')
            and   name  = 'PLAYLISTTRACKS_FK'
            and   indid > 0
            and   indid < 255)
   drop index PLAYLISTTRACKS.PLAYLISTTRACKS_FK
go

if exists (select 1
            from  sysobjects
           where  id = object_id('PLAYLISTTRACKS')
            and   type = 'U')
   drop table PLAYLISTTRACKS
go

if exists (select 1
            from  sysobjects
           where  id = object_id('TRACKS')
            and   type = 'U')
   drop table TRACKS
go

if exists (select 1
            from  sysobjects
           where  id = object_id('USERS')
            and   type = 'U')
   drop table USERS
go

/*==============================================================*/
/* Table: PLAYLIST                                              */
/*==============================================================*/
create table PLAYLIST (
   ID                   int                  not null,
   USERNAME             char(255)            not null,
   NAME                 char(255)            not null,
   OWNER                bit                  not null,
   LENGTH               int                  not null,
   constraint PK_PLAYLIST primary key (ID)
)
go

/*==============================================================*/
/* Index: USERPLAYLIST_FK                                       */
/*==============================================================*/




create nonclustered index USERPLAYLIST_FK on PLAYLIST (USERNAME ASC)
go

/*==============================================================*/
/* Table: PLAYLISTTRACKS                                        */
/*==============================================================*/
create table PLAYLISTTRACKS (
   ID                   int                  not null,
   TRACK_ID             int                  not null,
   OFFLINEAVAILABLE     bit                  not null

   constraint PK_PLAYLISTTRACKS primary key (ID, TRACK_ID)
)
go

/*==============================================================*/
/* Index: PLAYLISTTRACKS_FK                                     */
/*==============================================================*/




create nonclustered index PLAYLISTTRACKS_FK on PLAYLISTTRACKS (ID ASC)
go

/*==============================================================*/
/* Index: PLAYLISTTRACKS2_FK                                    */
/*==============================================================*/




create nonclustered index PLAYLISTTRACKS2_FK on PLAYLISTTRACKS (TRACK_ID ASC)
go

/*==============================================================*/
/* Table: TRACKS                                                */
/*==============================================================*/
create table TRACKS (
   TRACK_ID             int                  not null,
   TITLE                char(255)            not null,
   PERFORMER            char(255)            not null,
   ALBUM                char(255)            null,
   PLAYCOUNT            int                  not null,
   PUBLICATIONDATE      datetime             null,
   DESCRIPTION          char(255)            null,
   OFFLINEAVAILABLE     bit                  not null,
   DURATION             int                  null,
   constraint PK_TRACKS primary key (TRACK_ID)
)
go

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS (
   USERNAME             char(255)            not null,
   PASSWORD             varchar(255)         not null,
   TOKEN                varchar(255)         null,
   constraint PK_USERS primary key (USERNAME)
)
go

alter table PLAYLIST
   add constraint FK_PLAYLIST_USERPLAYL_USERS foreign key (USERNAME)
      references USERS (USERNAME)
go

alter table PLAYLISTTRACKS
   add constraint FK_PLAYLIST_PLAYLISTT_PLAYLIST foreign key (ID)
      references PLAYLIST (ID)
go

alter table PLAYLISTTRACKS
   add constraint FK_PLAYLIST_PLAYLISTT_TRACKS foreign key (TRACK_ID)
      references TRACKS (TRACK_ID)
go

