//Szymon Ciula
#ifndef POLYNOMIAL_H
#define POLYNOMIAL_H


#include <string>
#include <cstdlib>
#include "Z3.h"

class Polynomial
{
public:
    Polynomial();
    virtual ~Polynomial();
    Polynomial(const unsigned int stopien, const Z3* wsp);
    Polynomial(const Polynomial& other);
    Polynomial& operator=(const Polynomial& other);
//operatory
    unsigned int operator[] (const unsigned int indeks) const;
    Polynomial& operator+= (const Polynomial& other);
    Polynomial& operator-= (const Polynomial& other);
    Polynomial& operator*= (const Polynomial& other);
    Polynomial& operator*= (const Z3& number);
    Polynomial& operator/= (const Z3& number);
//inne funkcje
    inline unsigned int degree() const  { return stopien; }
    std::string toString(const std::string& arg) const;
    Polynomial& redukuj();
private:
    unsigned int stopien;
    Z3* wspolczynniki;

    friend Polynomial operator+ (const Polynomial& a, const Polynomial& b);
    friend Polynomial operator- (const Polynomial& a, const Polynomial& b);
    friend Polynomial operator* (const Polynomial& a, const Polynomial& b);
    friend Polynomial operator* (const Z3& number, const Polynomial& p);
    friend Polynomial operator/ (const Polynomial& p, const Z3& number);
    friend std::ostream& operator<< (std::ostream& out, const Polynomial& p);
    friend void operator>> (std::istream& in, Polynomial& p);
    friend void mod(const Polynomial& u, const Polynomial& v, Polynomial& q, Polynomial& r);
};

Polynomial operator+ (const Polynomial& a, const Polynomial& b);
Polynomial operator- (const Polynomial& a, const Polynomial& b);
Polynomial operator* (const Polynomial& a, const Polynomial& b);
Polynomial operator* (const Z3& number, const Polynomial& p);
Polynomial operator/ (const Polynomial& p, const Z3& number);
std::ostream& operator<< (std::ostream& out, const Polynomial& p);
void operator>> (std::istream& in, Polynomial& p);
std::string itoa(unsigned int liczba);
void mod(const Polynomial& u, const Polynomial& v, Polynomial& q, Polynomial& r);

#endif // POLYNOMIAL_H
