package donemProjesi;

public class Main {
    public static void main(String[] args) {
        App gameApp = new App();
        //annemi ve kendimi yarattım :)
        User p1 = new User("natalia", "melisa123", UserType.PARENT);
        //User p2 = new User("unal", "melisa123", UserType.PARENT); 
        //ikinci bir parent oluştuğu zaman hata veriyor mu diye kontrol etmiştim
        User c1 = new User("melisa", "melisa123", UserType.CHILD);
        User c2 = new User("melis", "melisa123", UserType.CHILD);
        gameApp.getUsers().add(p1);
        gameApp.getUsers().add(c1);
        gameApp.getUsers().add(c2);
        //gameApp.getUsers().add(p2);

        // arayüzü çalıştırmak için yeni bir obje yaratıp ordan run fonksiyonunu çağırıyorum
        GUI gui = new GUI(gameApp);
        gui.run();
    }
}