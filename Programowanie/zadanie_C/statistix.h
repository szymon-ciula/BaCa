//Szymon Ciula
#ifndef STATISTIX_H_INCLUDED
#define STATISTIX_H_INCLUDED

bool expval(unsigned int number_of_propabilities, const double* fun_args_t, const double* propability_t, double (*fun)(double), double& r);
bool median(unsigned int array_size, const int* const numbers_t, int (*fun)(int), bool (*condition)(int), double& r);
unsigned int gcd(unsigned int array_size, const int* numbers_t, int* r = nullptr);
unsigned int count(unsigned int array_size, const int* numbers_t, bool (*condition)(int, int) = nullptr);

//Makes copy of non-empty array_origin skipping duplicates and saves it to array_target.
//Returns number of elements of created set.
unsigned int reduce_duplicates(int* const array_target, const int* const array_origin, const unsigned int array_size);
//Makes set with elements that meet the condition.
//Returns number of those elements.
unsigned int choose_good_numbers(const int* const numbers_t, int* const good_numbers, const unsigned int array_size, bool (*condition)(int));
//Returns minimal of absolute values of set's elements.
unsigned int get_minimal_abs(const int* const numbers_t, const unsigned int array_size);
//Returns GCD of elements from "numbers_t" array.
unsigned int get_gcd(const unsigned int minimal_abs, const int* const numbers_t, const unsigned int array_size);
void quick_sort(int* const array_beg, int* const array_end);


inline unsigned int abs_value(int number) {return ( number>=0 ? number : -number );}

bool expval(unsigned int number_of_propabilities, const double* fun_args_t, const double* propability_t, double (*fun)(double), double& r)
{
    double sum = 0;
    const double *ptr_prop = propability_t; //auxiliary variable to iterate
//calculating of the sum
    for(int i=number_of_propabilities; i>0; --i)
    {
        if(*ptr_prop < 0)
            return false;
        sum += *ptr_prop++;
    }
    if(sum >= 1)
        return false;
//calculating of finally result
    ptr_prop--; //pointer to the last element
    const double *ptr_args = fun_args_t+number_of_propabilities; //auxiliary variable to iterate
    r = fun(*ptr_args--) * (1-sum);

    for(int i=number_of_propabilities; i>0; --i)
        r += fun(*ptr_args--) * (*ptr_prop--);

    return true;
}

bool median(unsigned int array_size, const int* const numbers_t, int (*fun)(int), bool (*condition)(int), double& r)
{
    bool non_empty = false;
    int* const good_numbers = new int[array_size];
//choosing numbers which meet with condition
    unsigned int number_of_elements = choose_good_numbers(numbers_t, good_numbers, array_size, condition);

    if(number_of_elements > 0)
    {
        non_empty = true;
    //calculating value of function for good elements
        for(int i=number_of_elements, *ptr=good_numbers;  i>0;  --i, ++ptr)
            *ptr = fun(*ptr);
    //set reduction
        number_of_elements = reduce_duplicates(good_numbers, good_numbers, number_of_elements);
    //median calculation
        if(number_of_elements != 1)
        {
            quick_sort(good_numbers, good_numbers+number_of_elements-1);
            int *ptr = good_numbers + (number_of_elements>>1);

            if(number_of_elements%2 != 0)
                r = static_cast<double>(*ptr);
            else
                r = (static_cast<double>(*ptr) + static_cast<double>(*(ptr-1))) / 2;
        }
        else
            r = static_cast<double>(*good_numbers);
    }

    delete [] good_numbers;
    return non_empty;
}

unsigned int gcd(unsigned int array_size, const int* numbers_t, int* r)
{
    unsigned int greatest_common_divisor = 0;

    if(array_size != 0)
    {
        int* const good_numbers = new int [array_size]; //array for elements different than 0
        unsigned int number_of_good_el;
        const int *ptr = numbers_t; //auxiliary variable to iterate

    //choosing numbers different than 0
        number_of_good_el = choose_good_numbers(numbers_t, good_numbers, array_size, [](int a){return a!=0;});
        if(number_of_good_el != 0)
        {
        //searching for GCD
            greatest_common_divisor = get_gcd(  get_minimal_abs(good_numbers,number_of_good_el), good_numbers, number_of_good_el  );
        //copying values to *r
            if(r != nullptr)
            {
                ptr = numbers_t;
                for(int j=array_size; j>0; --j)
                    *r++ = (*ptr++)/greatest_common_divisor;
            }
        }

        delete [] good_numbers;
    }

    return greatest_common_divisor;
}

unsigned int count(unsigned int array_size, const int* numbers_t, bool (*condition)(int, int))
{
    unsigned int result = 0;

    if(array_size != 0)
    {
    //duplicats reduction
        int* const array_without_duplicates = new int[array_size];
        unsigned int number_of_elements = reduce_duplicates(array_without_duplicates, numbers_t, array_size);
    //counting pairs that meet with given condition
        if(condition != nullptr)
        {
            const int *ptr_2, *ptr_1 = array_without_duplicates;
            for(int i=number_of_elements; i>0; --i, ++ptr_1)
            {
                ptr_2 = array_without_duplicates;
                for(int j=number_of_elements; j>0; --j)
                {
                    if(condition(*ptr_1, *ptr_2++))
                        result++;
                }
            }
        }
    //If there is no condition, we count number of pairs;
        else
            result = number_of_elements*number_of_elements;

        delete [] array_without_duplicates;
    }

    return result;
}

unsigned int get_minimal_abs(const int* const numbers_t, const unsigned int array_size)
{
    unsigned int minimal = abs_value(*numbers_t);
    const int *ptr = numbers_t;

    for(int i=array_size; i>0; --i)
    {
        if(abs_value(*ptr) < minimal)
            minimal = abs_value(*ptr);
        ptr++;
    }

    return minimal;
}

unsigned int choose_good_numbers(const int* const numbers_t, int* const good_numbers, const unsigned int array_size, bool (*condition)(int))
{
    const int *ptr_origin = numbers_t; //auxiliary variable to iterate
    int *ptr_target = good_numbers; //auxiliary variable to iterate
    unsigned int target_array_size = 0;

    for(int i=array_size; i>0; --i)
    {
        if(condition(*ptr_origin))
        {
            *ptr_target++ = *ptr_origin;
            target_array_size++;
        }
        ptr_origin++;
    }

    return target_array_size;
}

unsigned int get_gcd(const unsigned int minimal_abs, const int* const numbers_t, const unsigned int array_size)
{
    unsigned int greatest_common_divisor = 1;
    bool whether_divisor;
    const int *ptr; //auxiliary variable to iterate

//We searching for gcd among divisors of minimal element.
    for(int i=minimal_abs; i>1; --i, whether_divisor=true)
    {
        if(minimal_abs%i == 0)
        {
            ptr = numbers_t;
        //checking whether the other numbers are divisible
            for(int j=array_size; j>0; --j, ++ptr)
            {
                if(*ptr%i != 0)
                {
                    whether_divisor = false;
                    break;
                }
            }
        //We iterate to 0, so we can break the loop, when we find any divisor.
            if(whether_divisor)
            {
                greatest_common_divisor = i;
                break;
            }
        }
    }
    return greatest_common_divisor;
}

unsigned int reduce_duplicates(int* const array_target, const int* const array_origin, const unsigned int array_size)
{
//auxiliary variable to iterate
    int *ptr_target = array_target;
    const int *ptr_origin = array_origin;
//array copying
    for(int i=array_size; i>0; --i)
        *ptr_target++ = *ptr_origin++;
//array sorting
    quick_sort(array_target, array_target+array_size-1);
//removing duplicates
    unsigned int target_size = 1;
    ptr_origin = ptr_target = array_target;
    ptr_origin++;
    for(int i=array_size; i>1; --i)
    {
        if(*ptr_origin != *ptr_target)
        {
            *(++ptr_target) = *ptr_origin;
            target_size++;
        }
        ptr_origin++;
    }

    return target_size;
}

void quick_sort(int* const array_beg, int* const array_end)
{
    int middle = *(array_beg + ((array_end-array_beg)>>1));
    int *left_ptr = array_beg;
    int *right_ptr = array_end;
//Looking for elements, which are on the wrong side of middle:
    do
    {
    //greater than middle but left of it and...
        while(*left_ptr < middle)
            left_ptr++;
    //lower than middle but right of it.
        while(*right_ptr > middle)
            right_ptr--;
    //swaping those elements
        if(left_ptr <= right_ptr)
        {
            int buff = *right_ptr;
            *right_ptr-- = *left_ptr;
            *left_ptr++ = buff;
        }
    } while(left_ptr <= right_ptr);
//sorting left and right sub-array
    if(left_ptr < array_end)
        quick_sort(left_ptr, array_end);
    if(right_ptr > array_beg)
        quick_sort(array_beg, right_ptr);
}

#endif // STATISTIX_H_INCLUDED
