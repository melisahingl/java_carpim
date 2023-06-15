package donemProjesi;
import java.util.Random;

public class Exercise{
    private int a=10;
    private int b=10;
    private int N=10;
    private int currentA;
    private int currentB;
//a,b,n değerleri ve soru oluşturma ile ilgili işlemler bu sınıfta gerçekleşiyor
    
    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }
   
    
    public int getCurrentA() {
		return currentA;
	}

	public void setCurrentA(int currentA) {
		this.currentA = currentA;
	}

	public int getCurrentB() {
		return currentB;
	}

	public void setCurrentB(int currentB) {
		this.currentB = currentB;
	}

	// rastgele sayılar ile soru oluşturmaya yarayan fonksiyon
	public String getQuestion() {
        Random rand = new Random();
        setCurrentA(rand.nextInt(a ) + 1);
        setCurrentB(rand.nextInt(b ) + 1);
        return getCurrentA() + "x" + getCurrentB() + " = ?";
    }
//oluşturulan sorunun cevabını döndüren fonksiyon
    public int getAnswer() {
        return getCurrentA() * getCurrentB(); 
    }
}