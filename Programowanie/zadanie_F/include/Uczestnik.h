#ifndef UCZESTNIK_H
#define UCZESTNIK_H

struct Uczestnik
{
    enum Plec{K,M,W};

    Uczestnik(Plec p) : plec(p) {}
    Plec plec;
};

#endif // UCZESTNIK_H
