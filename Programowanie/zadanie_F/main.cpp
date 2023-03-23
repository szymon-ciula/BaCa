#include <iostream>
#include "source.cpp"

using namespace std;

int main()
{
  Wodzirej w;

  Uczestnik p(Uczestnik::K);
  Uczestnik q(Uczestnik::M);
  Uczestnik r(Uczestnik::K);
  Uczestnik s(Uczestnik::M);
  Uczestnik t(Uczestnik::K);
  Uczestnik u(Uczestnik::M);

  cout << "####################\n";
  w.dolacz(&p);
  cout << w.dolacz(&q) << endl;
  w.dolacz(&r,0);
  w.dolacz(&s,1);
  w.dolacz(&t);
  w.dolacz(&u);
  cout << w.liczba() << endl;
  cout << "####################\n";
  w.uczestnicy();
  w.uczestnicy(Uczestnik::K);
  cout << "####################\n";
  w.zrezygnuj(2);
  w.uczestnicy();
  w.uczestnicy(Uczestnik::M);
  cout << "####################\n";

  return 0;
}
