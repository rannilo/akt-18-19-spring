package week10.interdemo.spammerast;

import week10.interdemo.Relay;

import java.util.Objects;

public class MailCmd extends RelayCommands {

    private String address;

    public MailCmd(String address, String name, String msg) {
        super(name, msg);
        this.address = address;
    }

    @Override
    public void send() {
        Relay.sendMail(address, name, msg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MailCmd mailCmd = (MailCmd) o;
        return Objects.equals(address, mailCmd.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address);
    }

    @Override
    public String toString() {
        return "MailCmd{" +
                "address='" + address + '\'' +
                ", msg='" + msg + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
