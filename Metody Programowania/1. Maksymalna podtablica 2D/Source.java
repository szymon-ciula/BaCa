//Szymon Ciula - 4
import java.util.Scanner;

public class Source
{
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String [] args)
    {
        byte n = sc.nextByte(); //liczba zestawow
        byte wiersze, kolumny;  //liczba kolumn i wierszy dla konkretnych zestawow

        for(byte i=1; i<=n; ++i)
        {
            sc.nextByte();
            sc.next();
            wiersze = sc.nextByte();
            kolumny = sc.nextByte();

            int[][] tab = new int[wiersze][kolumny];
            //Tworzymy pomocnicza tablice przechowujaca sumy podtablic zaczepionych w punkcie [0,0].
            //Ze wzgledow praktycznych (dodawanie w petli) chcemy miec kolumne i rzad wiecej -
            //dlatego prefix_sum[i+1][j+1] zawiera sume liczb podtablicy [0..i][0..j].
            long[][] prefix_sum = new long[wiersze+1][kolumny+1];

            //wprowadzanie tablicy
            for(byte j=0; j<wiersze; ++j)
            {
                for(byte k=0; k<kolumny; ++k)
                {
                    tab[j][k] = sc.nextInt();
                    //Obliczamy tablice pomocnicza.
                    prefix_sum[j+1][k+1] = tab[j][k] + prefix_sum[j+1][k] + prefix_sum[j][k+1] - prefix_sum[j][k];
                }
            }

            //Suma poczatkowo ustawiona na -1, aby nie bylo problemu z maksymalna podtablica o sumie 0.
            long obecna_suma, max_suma=-1;
            //wspolrzedne maksymalnej podtablicy w tablicy prefix_sum
            byte end_kolumna_b=0, end_kolumna_e=0, end_wiersz_b=0, end_wiersz_e=0;

            //Rozszerzamy algorytm Kadane do tablicy 2D, wspomogajac sie utworzona wczesniej tablica.
            for(byte wiersz=0; wiersz<wiersze; ++wiersz)
            {
                for(byte kon_wiersz=(byte)(wiersz+1); kon_wiersz<=wiersze; ++kon_wiersz)
                {
                    for(byte kolumna=1, beg_kol=0; kolumna<=kolumny; ++kolumna)
                    {
                        //Obliczanie podtablicy z pomoca wczesniej utworzonej tablicy sum.
                        //Dzieki temu nie musimy obliczac sumy po elementach.
                        obecna_suma = prefix_sum[kon_wiersz][kolumna] + prefix_sum[wiersz][beg_kol]
                                    - prefix_sum[kon_wiersz][beg_kol] - prefix_sum[wiersz][kolumna];

                        //Algorytm Kadane zmodyfikowany dla tablicy 2D i naszych zalozen
                        if(obecna_suma >= 0)
                        {
                            if(obecna_suma > max_suma)
                            {
                                max_suma = obecna_suma;
                                //przepisujemy iteratory
                                end_kolumna_b = beg_kol;    end_kolumna_e = kolumna;
                                end_wiersz_b = wiersz;      end_wiersz_e = kon_wiersz;
                            }
							//Jesli suma jest rowna, sprawdzamy, czy tablica ma mniej elementow.
							//Nie musimy sprawdzac porzadku leksykograficznego, bo nasze koordynaty tylko wzrastaja.
                            else if( (obecna_suma == max_suma) &&
									((kon_wiersz-wiersz)*(kolumna-beg_kol) < (end_wiersz_e-end_wiersz_b)*(end_kolumna_e-end_kolumna_b)))
                            {
                                //przepisujemy iteratory
                                end_kolumna_b = beg_kol;    end_kolumna_e = kolumna;
                                end_wiersz_b = wiersz;      end_wiersz_e = kon_wiersz;
                            }
                        }
						//Szukamy najmniejszej podtablicy, wiec przesuwamy zakres dla sumy=0.
						//Nasza najwieksza suma rowniez moze byc rowna 0,
						//dlatego nie dajemy "else" tylko 2-krotnie porownujemy z 0.
                        if(obecna_suma <= 0)
                                beg_kol = kolumna;
                    }
                }
            }

            //Wypisywanie wyjscia
            System.out.print(i+": n = "+wiersze+" m = "+kolumny+", s = ");
            if(max_suma != -1)
                System.out.print(max_suma+", mst = a["+end_wiersz_b+".."+(end_wiersz_e-1)+"]["+end_kolumna_b+".."+(end_kolumna_e-1)+"]\n");
            else
                System.out.print(0+", mst is empty\n");
        }
    }
}
