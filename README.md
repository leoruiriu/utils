# utils

## BitFlag
- 電子コミックの閲覧履歴管理をclient側からserver側に移行したいとの要望
- ユーザー×作品×話のレコードをMySQLに格納することはNG
- 32進数の一文字で5bitを表現できるので、64進数よりは計算が簡単
- 32進数を利用して、フラグ情報を圧縮(2000話のフラグだとしても、varchar(512)で余裕)
[source](https://github.com/wagstyle/utils/blob/master/BitFlag/Base32BitFlag.java)
