// Szymon Ciula

#include <iostream>
#include <cmath>
#include <iomanip>

using namespace std;

struct Triple
{
    float x1, x2, x3;
    Triple(float x1_, float x2_, float x3_) : x1{ x1_ }, x2{ x2_ }, x3{ x3_ } {}
};

ostream& operator<< (ostream& out, const Triple& triple)
{
    return (out << triple.x1 << ' ' << triple.x2 << ' ' << triple.x3);
}

Triple findTriple(float product, float sum);

int main()
{
    int N;
    float x1, x2, x3;
    float sum, product;


    cin >> N;

    for (int i = N; i > 0; --i)
    {
        cin >> product >> sum;
        cout << fixed << setprecision(10) << scientific << findTriple(product, sum) << '\n';
    }

    return 0;
}




Triple findTriple(float product, float sum)
{
    float x1, x2, q, delta1, delta2;
    x2 = cbrt(product);

    delta1 = (x2 + sum);
    delta2 = (sum - 3 * x2);
    if ((delta1<0 && delta2>0)  ||  (delta1>0 && delta2<0))
        return Triple(0, 0, 0);
    else if (delta1==0 || delta2==0)
    {
        x1 = (-x2 + sum) / 2;
        q = x2 / x1;
        if (x1 >= x2*q)
            return Triple(x1, x2, x2 * q);
        else
            return Triple(0, 0, 0);
    }
    else
    {
        float delta = sqrt(delta1)*sqrt(delta2);
        x1 = (-x2 + sum - delta) / 2;
        q = x2 / x1;
        if (x1 >= x2 * q)
            return Triple(x1, x2, x2 * q);

        x1 += delta;
        q = x2 / x1;
        if (x1 >= x2 * q)
            return Triple(x1, x2, x2 * q);
        else
            return Triple(0, 0, 0);
    }
}
