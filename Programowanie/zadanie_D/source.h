//Szymon Ciula
#ifndef SOURCE_H_INCLUDED
#define SOURCE_H_INCLUDED

#include <cstdarg>

void przeksztalc(char* const string_v, char (*fun)(char));
bool filtruj(char* const string_v, bool (*fun)(char));
void filtruj(char* const string_1, const char* const string_2);
bool filtruj(char** array_beg, char** const array_end, bool (*fun)(char));
void szyfruj(char* string_v, char* key);
void przetwarzaj(const char* arguments_key, ...);

char encrypt_letter(const char char_v, const char key, const char a);
bool check_key(const char* key);

#endif // SOURCE_H_INCLUDED
