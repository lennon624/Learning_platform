package com.xk.sb.common.tools.mail;

public class Email
{
    // 注册邮件
    public static final int TYPE_REGISTER = 0;
    // 重置密码邮件
    public static final int TYPE_RECOVERY = 1;

    private String nickname;
    private String info;
    private int type;
    private String subject;
    private String recipient;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}