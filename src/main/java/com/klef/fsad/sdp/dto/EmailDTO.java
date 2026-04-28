package com.klef.fsad.sdp.dto;

public class EmailDTO
{
    private String fullname;
    private String subject;
    private String message;
    private String receiveremail;
    private String contact;

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getReceiveremail()
    {
        return receiveremail;
    }

    public void setReceiveremail(String receiveremail)
    {
        this.receiveremail = receiveremail;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    @Override
    public String toString()
    {
        return "EmailDTO [fullname=" + fullname + ", subject=" + subject + ", message=" + message
                + ", receiveremail=" + receiveremail + ", contact=" + contact + "]";
    }
}
