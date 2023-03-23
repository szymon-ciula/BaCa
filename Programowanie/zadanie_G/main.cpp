#include "Z3.h"
#include "Polynomial.h"
#include <iostream>
#include <sstream>
using namespace std;
#define LOGGER(x) std::cout << (#x) << "=" << (x) << std::endl;

int main(){
  //Z3 x(2), y(4);

  Z3 a(0), b(1), c(2);
  Z3 tab[10] = {a,a,b,b,c,c,b,b,a,a};
  Z3 tab1[10] = {a,a,b,b,c,c,b,b,c,a};
  Z3 tab2[3] = {a,b,c};
  Z3 Aa[] = {1, 2, 0, 0, 1, 1, 1, 2, 1, 0, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, Bb[] = {0, 2, 1, 1};
  Polynomial A(24,Aa), B(3,Bb), x, q,r;
  mod(A,B,q,r);
  cout << A.toString("x") << endl << B.toString("x") << endl << (A*B).toString("x") << endl;
    A*=B;
    cout << A.toString("x") <<endl;
    cin >> A;
    cout << A.toString("x");
//    Polynomial v = Polynomial(9, tab);
//    Polynomial q,r, w=Polynomial(2,tab2);
//    mod(v,w,q,r);
//    cout << v << endl << w << endl << q << endl << r << endl;
    //cout << p.toString("x") << endl << p << endl << p.degree();
    //cout << p.toString("x") << endl << p.degree() << endl << p+p << endl << p*p << endl << p-p << endl << p/c << endl;

  /*LOGGER(x);
  LOGGER(y);
  LOGGER(x+y);
  LOGGER(x-y);
  LOGGER(x*y);
  LOGGER(x/y);

  std::istringstream in("litery 0001122233{0,2,1}");
  Polynomial w;
  in >> w;
  cout  << w << endl;
  cout << w.toString("x") << endl;

  const int N1 = 24;
  const int N2 = 3;
  Z3 d1[N1+1] = {Z3(1),Z3(5),Z3(15),Z3(0),Z3(1),Z3(1),Z3(1),Z3(20),Z3(1),Z3(0),Z3(0),Z3(1),Z3(1),Z3(20),Z3(1)};
  Z3 d2[N2+1] = {Z3(0),Z3(5),Z3(7),Z3(1)};
  d1[N1] = 1;
  Polynomial u(N1,d1), v(N2,d2);
  LOGGER(u);
  LOGGER(v);
  LOGGER(u+v);
  LOGGER(u-=v);
  LOGGER(u*v);
  LOGGER(u*v-v*u);
  LOGGER((u*v).toString("x"));

  Polynomial q, r;
  mod(u,v,q,r);
  LOGGER(q.toString("q"));
  LOGGER(r.toString("r"));*/

  return 0;
}
