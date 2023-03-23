//Szymon Ciula
#include "Polynomial.h"

Polynomial::Polynomial() : stopien{0}
{
	wspolczynniki = new Z3[1];
	wspolczynniki[0] = 0;
}

Polynomial::~Polynomial()
{
    delete [] wspolczynniki;
}
Polynomial::Polynomial(const unsigned int st, const Z3* wsp) : stopien{st}
{
	wspolczynniki = new Z3[stopien+1];
    for(unsigned int i=stopien; i>0; --i)
        wspolczynniki[i] = wsp[i];
    wspolczynniki[0] = wsp[0];

	this->redukuj();
}
Polynomial::Polynomial(const Polynomial& other) : stopien{other.stopien}, wspolczynniki{new Z3[stopien+1]}
{
    for(unsigned int i=stopien; i>0; --i)
        wspolczynniki[i] = other.wspolczynniki[i];
    wspolczynniki[0] = other.wspolczynniki[0];
}

Polynomial& Polynomial::operator= (const Polynomial& rhs)
{
    if(this != &rhs)
    {
        Z3* buff = wspolczynniki;

        wspolczynniki = new Z3[rhs.stopien];
        for(unsigned int i=rhs.stopien; i>0; --i)
            wspolczynniki[i] = rhs.wspolczynniki[i];
        wspolczynniki[0] = rhs.wspolczynniki[0];
        stopien = rhs.stopien;

        delete [] buff;
    }
    return *this;
}

unsigned int Polynomial::operator[] (const unsigned int indeks) const
{
    if(indeks<=stopien)
        return wspolczynniki[indeks];
    else
    {
        std::cout << "Niepoprawny indeks wielomianu\n";
        return wspolczynniki[0];
    }
}

Polynomial& Polynomial::operator+= (const Polynomial& other)
{
    if(stopien >= other.stopien)
    {
        for(unsigned int i=other.stopien; i>0; --i)
            wspolczynniki[i] += other.wspolczynniki[i];
        wspolczynniki[0] += other.wspolczynniki[0];
    }
    else
    {
        Z3* buff = wspolczynniki;
        wspolczynniki = new Z3[other.stopien+1];

        for(unsigned int i=stopien; i>0; --i)
            wspolczynniki[i] = buff[i]+other.wspolczynniki[i];
        wspolczynniki[0] = buff[0]+other.wspolczynniki[0];

        for(unsigned int i=stopien+1; i<=other.stopien; ++i)
            wspolczynniki[i] = other.wspolczynniki[i];

        stopien = other.stopien;
        delete [] buff;
    }

    return redukuj();
}
Polynomial& Polynomial::operator-= (const Polynomial& other)
{
    if(stopien >= other.stopien)
    {
        for(unsigned int i=other.stopien; i>0; --i)
            wspolczynniki[i] -= other.wspolczynniki[i];
        wspolczynniki[0] -= other.wspolczynniki[0];
    }
    else
    {
        Z3* buff = wspolczynniki;
        wspolczynniki = new Z3[other.stopien+1];

        for(unsigned int i=stopien; i>0; --i)
            wspolczynniki[i] = buff[i]-other.wspolczynniki[i];
        wspolczynniki[0] = buff[0]-other.wspolczynniki[0];

        for(unsigned int i=stopien+1; i<=other.stopien; ++i)
            wspolczynniki[i] = -other.wspolczynniki[i];

        stopien = other.stopien;
        delete [] buff;
    }

    return redukuj();
}
Polynomial& Polynomial::operator*= (const Polynomial& other)
{
    Z3* buff = wspolczynniki;
    wspolczynniki = new Z3[stopien+other.stopien+1];

    for(unsigned int i=stopien+other.stopien; i>0; --i)
        wspolczynniki[i].zeruj();
    wspolczynniki[0].zeruj();

    for(unsigned int i=0; i<=stopien; ++i)
    {
        for(unsigned int j=0; j<=other.stopien; ++j)
            wspolczynniki[i+j] += buff[i]*other.wspolczynniki[j];
    }

    stopien += other.stopien;
    delete [] buff;

    return this->redukuj();
}
Polynomial& Polynomial::operator*= (const Z3& number)
{
    switch(number)
    {
    case 0:
        *this = Polynomial();
        break;
    case 2:
        for(unsigned int i=stopien; i>0; --i)
            wspolczynniki[i] *= number;
        wspolczynniki[0] *= number;
    default:
        break;
    }

    return *this;
}
Polynomial& Polynomial::operator/= (const Z3& number)
{
    switch(number)
    {
    case 2:
        for(unsigned int i=stopien; i>0; --i)
            wspolczynniki[i] /= number;
        wspolczynniki[0] /= number;
        break;
    case 0:
        std::cout << "Dzielenie przez zero\n";
    default:
        break;
    }

    return *this;
}
std::string Polynomial::toString(const std::string& xVar) const
{
    std::string result = "";

    if(stopien==0 || wspolczynniki[0]!=0)
        result += static_cast<char>((short)wspolczynniki[0]+48);

    for(unsigned int i=1; i<=stopien; ++i)
    {
        if(wspolczynniki[i] != 0)
        {
            result += "+";
            result += static_cast<char>(((short)wspolczynniki[i])+48);
            result += "*";
            result += xVar + "^" + itoa(i);
        }
    }

    return ( result[0]!='+' ? result : result.substr(1,result.length()) );
}
//Funkcje globalne
Polynomial operator+ (const Polynomial& a, const Polynomial& b)
{
    const Polynomial *mniejszy, *wiekszy;
    if(a.stopien >= b.stopien)
    {
        mniejszy = &b;
        wiekszy = &a;
    }
    else
    {
        mniejszy = &a;
        wiekszy = &b;
    }

    Polynomial result = *wiekszy;
    for(unsigned int i=mniejszy->stopien; i>0; --i)
        result.wspolczynniki[i] += mniejszy->wspolczynniki[i];
    result.wspolczynniki[0] += mniejszy->wspolczynniki[0];

    return result.redukuj();
}
Polynomial operator- (const Polynomial& a, const Polynomial& b)
{
	Polynomial result = a;
	return (result -= b);
}
Polynomial operator* (const Polynomial& a, const Polynomial& b)
{
    Z3* wspolczynniki = new Z3[a.stopien+b.stopien+1];

	for(unsigned int i=a.stopien+b.stopien; i>0; --i)
		wspolczynniki[i].zeruj();
	wspolczynniki[0].zeruj();

    for(unsigned int i=0; i<=a.stopien; ++i)
    {
        for(unsigned int j=0; j<=b.stopien; ++j)
            wspolczynniki[i+j] += a.wspolczynniki[i]*b.wspolczynniki[j];
    }

    Polynomial result = Polynomial(a.stopien+b.stopien, wspolczynniki);

    delete [] wspolczynniki;

    return result.redukuj();
}
Polynomial operator* (const Z3& number, const Polynomial& p)
{
    switch(number)
    {
    case 1:
        return p;
    case 2:
      {
        Polynomial result = p;
        for(unsigned int i=p.stopien; i>0; --i)
            result.wspolczynniki[i] *= number;
        result.wspolczynniki[0] *= number;
        return result;
      }
    default:
        return Polynomial();
    }
}
Polynomial operator/ (const Polynomial& p, const Z3& number)
{
    switch(number)
    {
    case 2:
      {
        Polynomial result = p;
        for(unsigned int i=p.stopien; i>0; --i)
            result.wspolczynniki[i] /= number;
        result.wspolczynniki[0] /= number;
        return result;
      }
    case 0:
        std::cout << "Dzielenie przez zero\n";
    default:
        return p;
    }
}

std::ostream& operator<< (std::ostream& out, const Polynomial& p)
{
    out << '{';
    for(unsigned int i=0; i<p.stopien; ++i)
        out << p.wspolczynniki[i] << ',';
    out << p.wspolczynniki[p.stopien] << '}';
    return out;
}
void operator>> (std::istream& in, Polynomial& p)
{
    //while(std::cin.get() != '{');
    in.ignore(SIZE_MAX, '{');

    std::string wielomian;
    std::getline(in, wielomian);


    Z3* wspolczynniki = new Z3[wielomian.length()/2];

    for(size_t i=wielomian.length()-2; i>0; i-=2)
        wspolczynniki[i/2] = (wielomian[i]-48);
    wspolczynniki[0] = (wielomian[0]-48);

    p = Polynomial(wielomian.length()/2, wspolczynniki);
    delete [] wspolczynniki;
    p.redukuj();

    /*std::string wielomian;
    //cin >> std::ws();
    std::getline((in >> std::ws), wielomian);


    size_t beg=0;
    for(char c : wielomian)
    {
        if(c != '{')
            ++beg;
        else
            break;
    }

    Z3* wspolczynniki = new Z3[(wielomian.length()-beg)/2];

    unsigned int j=0;
    for(size_t i=beg+1; i<wielomian.length(); i+=2)
        wspolczynniki[j++] = Z3(static_cast<unsigned short>(wielomian[i]-'0'));

    p = Polynomial(j-1, wspolczynniki);
    delete [] wspolczynniki;
    p.redukuj();*/
}

void mod(const Polynomial& u, const Polynomial& v, Polynomial& q, Polynomial& r)
{
    if(u.stopien>=v.stopien && v.stopien!=0)
    {
        Polynomial roboczy = u;
        unsigned int indeks = u.stopien-v.stopien;
        Z3* wspolczynniki = new Z3[indeks+1];
        Z3 z3;

        for(unsigned int i=u.stopien; i>v.stopien; --i)
        {
            wspolczynniki[indeks--] = z3 = roboczy.wspolczynniki[i]/v.wspolczynniki[v.stopien];
            for(unsigned int j=i, k=v.stopien; k>0; )
                roboczy.wspolczynniki[j--] -= z3*v.wspolczynniki[k--];
            roboczy.wspolczynniki[i-v.stopien] -= z3*v.wspolczynniki[0];
        }
        wspolczynniki[0] = z3 = roboczy.wspolczynniki[v.stopien]/v.wspolczynniki[v.stopien];
        for(unsigned int j=v.stopien; j>0; --j)
            roboczy.wspolczynniki[j] -= z3*v.wspolczynniki[j];
        roboczy.wspolczynniki[0] -= z3*v.wspolczynniki[0];

        q = Polynomial(u.stopien-v.stopien, wspolczynniki);
        r = roboczy.redukuj();
        delete [] wspolczynniki;
    }
    else if(u.stopien < v.stopien)
    {
        q = Polynomial();
        r = u;
    }
    else
    {
        switch(v[0])
        {
        case 0:
            std::cout << "Dzielenie przez zero\n";
            break;
        case 1:
            q = u;
            r = Polynomial();
            break;
        default:
          {
            Z3 z3(2);
            q = u;
            r = Polynomial();
            for(unsigned int i=q.stopien; i>0; --i)
                q.wspolczynniki[i] /= z3;
            q.wspolczynniki[0] /= z3;
          }
        }
    }
}

Polynomial& Polynomial::redukuj()
{
    unsigned int st = stopien;
    while((wspolczynniki[st]==0) && (st!=0))
        --st;

    if(st != stopien)
    {
        stopien = st;
        Z3* buff = wspolczynniki;
        wspolczynniki = new Z3[st+1];

        for(unsigned int i=0; i<=st; ++i)
            wspolczynniki[i] = buff[i];

        delete [] buff;
    }

    return *this;
}

std::string itoa(unsigned int liczba)
{
    std::string cyfry = "";

    do
    {
        cyfry += static_cast<char>(liczba%10 + 48);
        liczba /= 10;
    } while(liczba>0);

    char buff;
    unsigned int dlugosc = cyfry.length()-1;
    for(unsigned int i=0; i*2<dlugosc; ++i)
    {
        buff = cyfry[i];
        cyfry[i] = cyfry[dlugosc-i];
        cyfry[dlugosc-i] = buff;
    }

    return cyfry;
}
