#include <iostream>
#include "Morfologia.h"
using namespace std;
int main()
{
    BitmapaExt b(3,4,5);
    b(0,1,0)= b(0,2,2) = b(0,2,3) = b(0,1,2)= true;
    b(1,2,1) = b(1,2,2) = b(1,2,3) = b(1,1,1) = b(2,1,2)= true;
    b(2,2,1) = b(2,2,2) = b(2,2,3) = b(2,1,1) = true;

    std::cout << "Oryginalna bitmapa:\n" <<  b << "\n\n";

    Usrednianie u;
    Dylatacja d;
    Inwersja i;
    Zerowanie z;
    Erozja e;
    i.przeksztalc(b);
    cout << b << endl;
    e.przeksztalc(b);
        cout << b << endl;

    z.przeksztalc(b);
        cout << b << endl;

    //ZlozeniePrzeksztalcen z;
    //z.dodajPrzeksztalcenie(&u);
    //z.dodajPrzeksztalcenie(&d);

// przeksztalcenie 'z' najpierw wykona usrednianie, a potem dylatacje
    //z.przeksztalc(b);

    std::cout << "Przeksztalcona bitmapa:\n" << b << "\n";

    return 0;
}
