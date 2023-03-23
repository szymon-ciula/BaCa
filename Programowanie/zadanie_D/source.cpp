//Szymon Ciula
#include "source.h"
//Realize function "fun" for all characters from string_v.
void przeksztalc(char* const string_v, char (*fun)(char))
{
    if(fun != 0  &&  string_v != 0)
    {
        char* ptr = string_v;
        while(*ptr != '\0')
        {
            *ptr = fun(*ptr);
            ptr++;
        }
    }
}

//Removes characters that meet with condition inside fun.
//Returns true, if string_v has been modified.
bool filtruj(char* const string_v, bool (*fun)(char))
{
    bool whether_modified = false;

    if(fun != 0  &&  string_v != 0)
    {
    //auxiliary variables to iterate
        char *ptr_target = string_v;
        const char *ptr_origin = string_v;

        while(*ptr_origin != '\0')
        {
            if( !fun(*ptr_origin) )
                *ptr_target++ = *ptr_origin;
            else
                whether_modified = true;
            ptr_origin++;
        }

        *ptr_target = '\0';
    }

    return whether_modified;
}

//Removes characters which are inside string_2.
void filtruj(char* const string_1, const char* const string_2)
{
    if(string_1 != 0  &&  string_2 != 0)
    {
    //auxiliary variables
        char* ptr_target = string_1;
        const char* ptr_origin_1 = string_1;
        const char* ptr_origin_2;
        char char_v;
        bool different;

        while(*ptr_origin_1 != '\0')
        {
            ptr_origin_2 = string_2;
            char_v = *ptr_origin_1;
            different = true;
        //checking if the letter is inside string_2
        //if not, we rewrite.
            while(*ptr_origin_2 != '\0')
            {
                if(char_v == *ptr_origin_2++)
                {
                    different = false;
                    break;
                }
            }

            if(different)
                *ptr_target++ = char_v;
            ptr_origin_1++;
        }

        *ptr_target = '\0';
    }
}

//Realize function "filtruj(char, bool (*)(char))" for string between _beg and _end.
//Return true, if any string has been modified.
bool filtruj(char** array_beg, char** const array_end, bool (*fun)(char))
{
    bool whether_modified = false;

    if( !(fun == 0  ||  array_beg == 0  ||  array_end == 0) )
    {
        while(array_beg < array_end)
            whether_modified = filtruj(*array_beg++, fun);
    }

    return whether_modified;
}

//As variable "a" we give char 'A', if char_v is a capital letter, otherwise -- 'a'.
char encrypt_letter(const char char_v, const char key, const char a)
{
    char shift = (key-'A') + (char_v-a);
    if(shift >= 26)  shift -= 26;
    return a+shift;
}

//Checks whether key is of good format.
bool check_key(const char* key)
{
    if(key == 0)     return false;
    if(*key == '\0') return false;

    while(*key != '\0')
    {
        if(*key<'A' || *key>'Z')
            return false;
        key++;
    }

    return true;
}

//Encryption using Vigenere cipher.
void szyfruj(char* string_v, char* key)
{
    if(string_v != 0)
    {
        //if(check_key(key))
        //{
            char* key_ptr = key;
            char char_v;

            while(*string_v != '\0')
            {
            //We have to multiplicate key, if it is shorter than encrypting sentence.
                if(*key_ptr == '\0')
                    key_ptr = key;
                char_v = *string_v;
            //The key is written with capital letters but our sentence -- not necessarily.
            //The algorithm is dependent on letters' size.
                if(char_v>='a' && char_v<='z')
                    *string_v = encrypt_letter(char_v, *key_ptr++, 'a');
                else if(char_v>='A' && char_v<='Z')
                    *string_v = encrypt_letter(char_v, *key_ptr++, 'A');

                string_v++;
            }
        //}
    }
}

void przetwarzaj(const char* arguments_key, ...)
{
    if(arguments_key != 0)
    {
        va_list ap;
        va_start(ap, arguments_key);
    //auxiliary variables
        char *string_v, *key;
        bool (*fun_bool)(char);
        char (*fun_char)(char);

        while(*arguments_key != '\0')
        {
            switch(*arguments_key)
            {
            case 'f':
                string_v = va_arg(ap, char*);
                fun_bool = va_arg(ap, bool (*)(char));
                filtruj(string_v, fun_bool);
                break;
            case 'p':
                string_v = va_arg(ap, char*);
                fun_char = va_arg(ap, char (*)(char));
                przeksztalc(string_v, fun_char);
                break;
            case 's':
                string_v = va_arg(ap, char*);
                key = va_arg(ap, char*);
                szyfruj(string_v, key);
                break;
            default:
                break;
            }
            arguments_key++;
        }

        va_end(ap);
    }
}

