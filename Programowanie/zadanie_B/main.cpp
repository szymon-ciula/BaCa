//Szymon Ciula
#include <iostream>
#include <algorithm>

using namespace std;
typedef unsigned int Uint;

//ptr_arr - our 2D-array; ptr_dim - our array of rows' lengths
//------------------------------------------------------------
void print(const Uint** ptr_arr, const Uint* ptr_dim, const Uint arr_size);
//In this function: first you give values of cells, then you give size of row.
void fill_in(Uint** ptr_arr, Uint* ptr_dim, const Uint arr_size);
//adds values of row at the end of row, e.g.: {1,2} -> {1,2,1,2}
void double_row(Uint** ptr_arr, Uint* ptr_dim);
void swap_rows(Uint** ptr_arr, Uint* ptr_dim);
//------------------------------------------------------------
//Next functions do NOT reallocate memory for ptr_arr, because we operate on a copy of ptr, but reallocate memory for *ptr_arr
//makes new row -- copy of row with given index -- at the end of array
//append_row() does not change size of array and demands reallocation before calling (in contrast to remove_row() and insert_to_row())
void append_row(Uint** ptr_arr, Uint* ptr_dim, const Uint arr_size);
//you have to send row_index here, because of calling this function in insert_to_row()
void remove_row(Uint** ptr_arr, Uint* ptr_dim, Uint& arr_size, const Uint row_index);
//inserts values of row_1 to row_2 starting from the given index; at the end row_1 is removing
void insert_to_row(Uint** ptr_arr, Uint* ptr_dim, Uint& arr_size);

int main()
{
    Uint row_index; //necessary for operation R
    Uint arr_size; //amount of rows
    cin >> arr_size;

    Uint *dimensions = static_cast<Uint*>( malloc(arr_size*sizeof(Uint)) ); //storage of array sizes
    Uint **arr = static_cast<Uint**>( malloc(arr_size*sizeof(Uint*)) ); //2D array

    fill_in(arr, dimensions, arr_size);

    char operation;
    do
    {
        cin >> operation; //P,S,D,R,A,I,E
        switch(operation)
        {
        case 'P':
            print(const_cast<const Uint**>(arr), const_cast<const Uint*>(dimensions), arr_size);
            break;

        case 'S':
            swap_rows(arr, dimensions);
            break;

        case 'D':
            double_row(arr, dimensions);
            break;

        case 'R':
            cin >> row_index;
            remove_row(arr, dimensions, arr_size, row_index);
            arr = static_cast<Uint**>( realloc(arr, arr_size*sizeof(Uint*)) );
            dimensions = static_cast<Uint*>( realloc(dimensions, arr_size*sizeof(Uint)) );
            break;

        case 'A':
            arr = static_cast<Uint**>( realloc(arr, (++arr_size)*sizeof(Uint*)) );
            dimensions = static_cast<Uint*>( realloc(dimensions, arr_size*sizeof(Uint)) );
            append_row(arr, dimensions, arr_size);
            break;

        case 'I':
            insert_to_row(arr, dimensions, arr_size);
            arr = static_cast<Uint**>( realloc(arr, arr_size*sizeof(Uint*)) );
            dimensions = static_cast<Uint*>( realloc(dimensions, arr_size*sizeof(Uint)) );
            break;

        default:
            break;
        }
    } while(operation != 'E');

//memory deallocation
    for(Uint i=arr_size, **ptr_t=arr; i>0; --i)
        free(*ptr_t++);
    free(arr);
    free(dimensions);

    return 0;
}

void fill_in(Uint** ptr_arr, Uint* ptr_dim, const Uint arr_size)
{
    for(Uint i=arr_size, *ptr; i>0; --i)
    {
        cin >> *ptr_dim;
    //allocating memory for single row
        ptr = *ptr_arr = static_cast<Uint*>( malloc((*ptr_dim)*sizeof(Uint)) );
        ptr_arr++;

        for(Uint j=*ptr_dim++; j>0; --j)
            cin >> *ptr++;
    }
}

void print(const Uint** ptr_arr, const Uint* ptr_dim, const Uint arr_size)
{
    for(Uint i=arr_size, *ptr; i>0; --i)
    {
        ptr = const_cast<Uint*>( *ptr_arr++ );
        for(Uint j=*ptr_dim++; j>0; --j)
            cout << *ptr++ << ' ';
        cout << '\n';
    }
    cout << '\n';
}

void double_row(Uint** ptr_arr, Uint* ptr_dim)
{
    Uint row_index;
    cin >> row_index;

    const Uint row_size = *(ptr_dim+row_index); //auxiliary variable
//reallocating memory of double size
//making auxiliary variables to iterate
    Uint *ptr_origin = *(ptr_arr+row_index) = static_cast<Uint*>( realloc(*(ptr_arr+row_index), row_size*sizeof(Uint)*2) );
    Uint *ptr_target = ptr_origin+row_size;
//copying first half of elements to second half of elements
    for(Uint i=row_size; i>0; --i)
          *ptr_target++ = *ptr_origin++;
//double size of row
    *(ptr_dim+row_index) <<= 1;
}

void swap_rows(Uint** ptr_arr, Uint* ptr_dim)
{
    Uint row1_index, row2_index;
    cin >> row1_index >> row2_index;
    swap(*(ptr_dim+row1_index), *(ptr_dim+row2_index));
    swap(*(ptr_arr+row1_index), *(ptr_arr+row2_index));
}

void append_row(Uint** ptr_arr, Uint* ptr_dim, const Uint arr_size)
{
    Uint row_index;
    cin >> row_index;

    *(ptr_dim+arr_size-1) = *(ptr_dim+row_index); //copying size of row
    *(ptr_arr+arr_size-1) = static_cast<Uint*>( malloc(*(ptr_dim+row_index)*sizeof(Uint)) ); //new row memory allocation
//auxiliary variables to iterate
    Uint *ptr_origin = *(ptr_arr+row_index); //through reading elements
    Uint *ptr_target = *(ptr_arr+arr_size-1); //through writing elements
//value copying
    for(Uint i=*(ptr_dim+row_index); i>0; --i)
        *ptr_target++ = *ptr_origin++;
}

void remove_row(Uint** ptr_arr, Uint* ptr_dim, Uint& arr_size, const Uint row_index)
{
//auxiliary variables
    Uint **ptr_row = ptr_arr+row_index; //to iterate
    Uint *ptr_size = ptr_dim+row_index; //to iterate
    Uint* const removed_row = *ptr_row; //to remember ptr of removing row

//rows shifting and changing of array size
    for(Uint i=(--arr_size)-row_index; i>0; --i)
    {
        *ptr_row = *(ptr_row+1);
        *ptr_size = *(ptr_size+1);
        ptr_row++;
        ptr_size++;
    }
//memory dealocation
    free(removed_row);
}

void insert_to_row(Uint** ptr_arr, Uint* ptr_dim, Uint& arr_size)
{
//cutted_row we will call "row1", and target_row -- "row2"
    Uint cutted_row_index, target_row_index, insert_index;
    cin >> cutted_row_index >> target_row_index >> insert_index;

//auxiliary variables
    const Uint row1_length = *(ptr_dim+cutted_row_index);
    const Uint row2_length = *(ptr_dim+target_row_index);
    Uint** const ptr_target = ptr_arr+target_row_index;

//allocating memory for longer row
    *ptr_target = static_cast<Uint*>( realloc(*ptr_target, (row1_length+row2_length)*sizeof(Uint)) );
//change of the value in the dimensions array
    *(ptr_dim+target_row_index) += row1_length;

//copying elements to the rigth of insert_index to the end of array
    Uint *ptr_el_origin = (*ptr_target + row2_length-1);              //auxiliary variable to iterate through reading elements
    Uint *ptr_el_target = (*ptr_target + row1_length+row2_length-1); //auxiliary variable to iterate through writing elements
    for(Uint i=row2_length-insert_index; i>0; --i)
        *ptr_el_target-- = *ptr_el_origin--;

//set pointers to the right place
    ptr_el_target = (++ptr_el_origin);
    ptr_el_origin = *(ptr_arr+cutted_row_index);
//copying elements from cutted_row
    for(Uint i=row1_length; i>0; --i)
        *ptr_el_target++ = *ptr_el_origin++;
//removing row
    remove_row(ptr_arr, ptr_dim, arr_size, cutted_row_index);
}

