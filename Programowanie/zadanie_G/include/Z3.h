//Szymon Ciula
#ifndef Z3_H
#define Z3_H

#include <iostream>

class Z3
{
public:
//konstruktory, destruktor i operator przypisania
    Z3();
    Z3(const short int number);
    ~Z3();
    Z3(const Z3& other);
    Z3& operator= (const Z3& other);
//operatory
    Z3& operator+= (const Z3& other);
    Z3& operator-= (const Z3& other);
    Z3& operator*= (const Z3& other);
    Z3& operator/= (const Z3& other);
    operator short int() const;

	void zeruj();

private:
    short int liczba;

	friend Z3 operator- (const Z3& z3);
    friend Z3 operator+ (const Z3& a, const Z3& b);
    friend Z3 operator- (const Z3& a, const Z3& b);
    friend Z3 operator* (const Z3& a, const Z3& b);
    friend Z3 operator/ (const Z3& a, const Z3& b);
    friend std::ostream& operator<< (std::ostream& out, const Z3& z3);
};

std::ostream& operator<< (std::ostream& out, const Z3& z3);
Z3 operator+ (const Z3& a, const Z3& b);
Z3 operator- (const Z3& a, const Z3& b);
Z3 operator* (const Z3& a, const Z3& b);
Z3 operator/ (const Z3& a, const Z3& b);

#endif // Z3_H
