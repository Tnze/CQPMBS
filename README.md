# CQPMBS
CQP-Minecraft Bukkit/Spigot插件
[回形针工作室]
<让正在服务器游玩的小伙伴在不退出游戏的情况下看到Q群里的消息>
[需要Java8]
该插件可以把在Q群里说话的玩家说的话发到服务器里面就像是他在游戏里说一样,如图:

[插件配置信息]
该插件的配置文件以及数据文件储存在/plugins/CQP文件夹里，而且不可直接编辑，只能通过命令配置。
将插件拖到plugins文件夹里后，需要手动新建CQP文件夹，否则会报错。
该插件依靠酷Q获取Q群的消息，所以需要先配置酷Q~

酷Q配置方法：


服务器插件配置方法：
酷Q成功运行后，即可正式开始配置本插件。


绑定成员名称：
本插件支持将玩家绑定QQ，绑定后该QQ在群内发言，转发至服务器时将会使用该玩家的游戏名，否则将显示该QQ号。
绑定方法：


关于该插件全部命令的说明

以下是所有可用的命令{大括号内的为命令的说明}：
/CQP bind {用于将QQ绑定到玩家名下，直接输入此命令，然后在Q群内发送验证码即可绑定成功}
/CQP delete [QQ号] {用于将QQ从本人名下删除。}
/CQP list {列出本人名下所有的QQ}
/CQP OP 
{
本命令需要OP权限
  使用方法：
/CQP OP setGroupID [Q群号] {设置服务器的群，请注意登录酷Q的QQ当然一定至少要是群成员}
  /CQP OP OP_M_delete [Q号] {从任意玩家名下解绑此QQ，用于Q号被盗绑时的找回}
}


[安全信息]
服务器请开启11235udp端口的防火墙，以免外来udp包接入、控制酷Q SocketAPI
好了，这就是这个插件的介绍咯~
