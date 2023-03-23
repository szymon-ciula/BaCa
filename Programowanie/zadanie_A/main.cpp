//Szymon Ciula

#include <iostream>

using namespace std;

//global variables
uint32_t board[1024][1024];
uint16_t board_size;
//classes
class List_1024;
class List_element;
//functions
void move_operation(List_1024& lista, const char& operation);   //for operations: L,P,G,D
void move_line(uint32_t *ptr_element, List_1024& lista, const int&& shift); //moves and reduces only a single line
void show_board();
int64_t add_to_board();     //returns difference of finally sum
uint64_t change_board();    //returns sum of board's elements

class List_element
{
    uint32_t value;
    List_element* previous;
    List_element* next;
public:
    List_element(uint32_t *val, List_element* prev) :   value{*val}, previous{prev}, next{nullptr}      {}

    inline List_element* get_previous() {return previous;}
    inline List_element* get_next() {return next;}
    inline const uint32_t& get_value() const {return value;}

    friend List_1024;
};

class List_1024
{
    List_element *first_element = nullptr;
    List_element *last_element = nullptr;
public:
    inline List_element* get_first() {return first_element;}
    inline List_element* get_last()  {return last_element;}

    void add_element(uint32_t* value);  //adds element at the end of the list
    void remove_element(List_element* element); //removes sent element from the list
    void reduce();  //reduces list to minimal form
    void clean();   //dealocating memory and change the list to initial form
};

int main()
{
    char operation = 0;
    uint64_t sum = 0;   //sum of board's elements
    List_1024 lista;

    cin >> board_size;
    board_size--;
    //then we don't have to make subtraction "board_size-1" every time
    //the same operation in change_board()

    while(operation != 'K')
    {
        cin >> operation;
        switch(operation)
        {
        case 'L':
        case 'P':
        case 'G':
        case 'D': move_operation(lista, operation); break;
        case 'N': sum += add_to_board(); break;
        case 'S':
        case 'K': cout << sum << '\n'; break;
        case 'C': sum = change_board(); break;
        case 'W': show_board(); break;
        default: break;
        }
    }

    return 0;
}

void move_operation(List_1024& lista, const char& operation)
{
    uint32_t *ptr_element;

    switch(operation)
    {
    case 'L':
        ptr_element = &(board[0][0]);
        for(int32_t i=board_size; i>=0; --i)
        {
            move_line(ptr_element, lista, 1);
            ptr_element += 1024;
        }
        break;

    case 'P':
        ptr_element = &(board[0][board_size]);
        for(int32_t i=board_size; i>=0; --i)
        {
            move_line(ptr_element, lista, -1);
            ptr_element += 1024;
        }
        break;

    case 'G':
        ptr_element = &(board[0][0]);
        for(int32_t j=board_size; j>=0; --j)
        {
            move_line(ptr_element, lista, 1024);
            ptr_element++;
        }
        break;

    case 'D':
        ptr_element = &(board[board_size][0]);
        for(int32_t j=board_size; j>=0; --j)
        {
            move_line(ptr_element, lista, -1024);
            ptr_element++;
        }
        break;

    default:
        break;
    }
}

void move_line(uint32_t *ptr_element, List_1024& lista, const int&& shift)
{
    uint32_t *ptr_begin = ptr_element;
//adding non-zero elements to list
    for(int i=board_size; i>=0; --i)
    {
        if(*ptr_element != 0)
            lista.add_element(ptr_element);
        ptr_element += shift;
    }
//reduction
    lista.reduce();
//copy from list to board
    List_element *ptr_lelement = lista.get_first();
    int i = board_size;

    while(ptr_lelement != nullptr)
    {
        *ptr_begin = ptr_lelement->get_value();
        ptr_lelement = ptr_lelement->get_next();
        ptr_begin += shift;
        i--;
    }
//complementing another cells with zeros
    for( ; i>=0; --i)
    {
        *ptr_begin = 0;
        ptr_begin += shift;
    }

    lista.clean();
}

void show_board()
{
    uint32_t *ptr_element = &(board[0][0]);

    for(int32_t i=board_size; i>=0; --i)
    {
        for(int32_t j=board_size; j>=0; --j)
            cout << *ptr_element++ << ' ';
        cout << '\n';
        ptr_element += (1023-board_size);
    }
}

int64_t add_to_board()
{
    int64_t sum = 0;
    uint16_t x, y;

    cin >> x >> y;
    sum -= board[x][y];
    cin >> board[x][y];

    return (sum + board[x][y]);
}

uint64_t change_board()
{
    uint64_t sum = 0;
    uint32_t *ptr_element = &(board[0][0]);

    cin >> board_size;
    board_size--;

    for(int32_t i=board_size; i>=0; --i)
    {
        for(int32_t j=board_size; j>=0; --j)
        {
            cin >> *ptr_element;
            sum += *ptr_element++;
        }
        ptr_element += (1023-board_size);
    }

    return sum;
}

//List_1024 functions
void List_1024::add_element(uint32_t* value)
{
    if(first_element != nullptr)
        last_element = last_element->next = new List_element(value, last_element);
    else
        last_element = first_element = new List_element(value, nullptr);
}

void List_1024::remove_element(List_element* element)
{
    if(element->next != nullptr)
    {
        if(element->previous != nullptr)
        {
            element->next->previous = element->previous;
            element->previous->next = element->next;
        }
        else
        {
            element->next->previous = nullptr;
            first_element = element->next;
        }
    }
    else
    {
        if(element->previous != nullptr)
        {
            element->previous->next = nullptr;
            last_element = element->previous;
        }
        else
        {
            first_element = nullptr;
            last_element = nullptr;
        }
    }
    delete element;
}

void List_1024::clean()
{
    first_element = nullptr;
    List_element *element = last_element;

    while(last_element != nullptr)
    {
        element = last_element;
        last_element = last_element->previous;
        delete element;
    }
}

void List_1024::reduce()
{
    List_element* ptr_lelement = first_element;

    if(ptr_lelement != nullptr)
    {
        while(ptr_lelement->next != nullptr)
        {
            if(ptr_lelement->value == ptr_lelement->next->value)
            {
                ptr_lelement->value <<= 1;
                remove_element(ptr_lelement->next);

                //it check if there is sth similar before
                while((ptr_lelement->previous != nullptr) && (ptr_lelement->value == ptr_lelement->previous->value))
                {
                    ptr_lelement->value <<= 1;
                    remove_element(ptr_lelement->previous);
                }
            }
            else
                ptr_lelement = ptr_lelement->next;
        }
    }
}

