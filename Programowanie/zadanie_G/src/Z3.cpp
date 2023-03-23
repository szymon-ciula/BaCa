//Szymon Ciula
#include "Z3.h"

Z3::Z3() : liczba{0}    {}
Z3::Z3(const short int number) : liczba{number>=0 ? static_cast<short>(number%3) : static_cast<short>((number%3+3)%3)}   {}
Z3::~Z3()   {}
Z3::Z3(const Z3& other) : liczba{other.liczba}  {}

Z3& Z3::operator= (const Z3& rhs)		{ liczba = rhs.liczba; return *this; }
Z3& Z3::operator+= (const Z3& other)    { liczba = (liczba+other.liczba)%3; return *this; }
Z3& Z3::operator-= (const Z3& other)    { liczba = (liczba>=other.liczba ? liczba-other.liczba : 3+liczba-other.liczba); return *this; }
Z3& Z3::operator*= (const Z3& other)    { liczba = (liczba*other.liczba)%3; return *this; }
Z3& Z3::operator/= (const Z3& other)
{
    if(other.liczba != 0)
        liczba = (liczba*other.liczba)%3;
    else
        std::cout << "Dzielenie przez zero\n";
    return *this;
}
Z3::operator short int() const  { return liczba; }
Z3 operator- (const Z3& z3)	{ return (Z3()-z3); }
Z3 operator+ (const Z3& a, const Z3& b) { return Z3((a.liczba+b.liczba)%3); }
Z3 operator- (const Z3& a, const Z3& b) { return Z3( (a.liczba>=b.liczba ? a.liczba-b.liczba : 3+a.liczba-b.liczba) ); }
Z3 operator* (const Z3& a, const Z3& b) { return Z3((a.liczba*b.liczba)%3); }
Z3 operator/ (const Z3& a, const Z3& b)
{
    if(b.liczba != 0)
        return a*b;
    std::cout << "Dzielenie przez zero\n";
    return a;
}
std::ostream& operator<< (std::ostream& out, const Z3& z3)  { return (out << z3.liczba); }

void Z3::zeruj() { liczba = 0; }
