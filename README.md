# movieWebService
SOAP Web Service provider for streaming and managing movies from Humber's Oracle DB using Java EE
{A movie cannot contain the word 'disney'}


Database Name: MovieDB
Tables:
..................................
Videos - 
ID	NUMBER(38,0)
TITLE	VARCHAR2(255 BYTE)
TYPE	VARCHAR2(255 BYTE)
GENRE	VARCHAR2(255 BYTE)
YEAR	NUMBER(38,0)
STAR_ACTOR_FK	NUMBER(38,0)
IMAGE	BLOB

...................................
Star_Actor -
ID	NUMBER(38,0)
FIRSTNAME	VARCHAR2(150 BYTE)
LASTNAME	VARCHAR2(150 BYTE)

...................................
Admin -
ID	NUMBER
USERNAME	VARCHAR2(255 BYTE)
PASSWORD	VARCHAR2(255 BYTE)