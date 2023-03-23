//Szymon Ciula
#include <iostream>
#include <cstring>
#include <cmath>

using namespace std;

struct Tree;
struct Root;

//Translate string to tree-struct.
void make_tree(Tree& tree, const char* exp);
//Sub-function of make_tree().
const char* make_root(Root* root, const char* exp);
//Sub-function of make_root().
const char* make_one_side(Root* root, const char* ptr);
//Return string with names of variables inside tree.
void take_variables(const Root* root, string& var);
//Some functions that are using inside main switch.
void standard_fun(Tree& tree, string& expression, string& variables, bool* tab);
//Checks if an expression inside tree is true for valuating inside tab.
bool if_true(const Root* root, const bool* tab);
//Returns the number of true out of all possible evaluations.
int how_many_true(const Tree& tree, bool* tab, const char* var);
//Checks if an expression is a tautology.
bool tautology(const Tree& tree, bool* tab, const char* var);

struct Root
{
    char symbol;
    short neg = 0;
    Root* left = nullptr;
    Root* right = nullptr;

    Root() = default;
    ~Root()
    {
        if(left)    delete left;
        if(right)   delete right;
    }

    inline Root* add_left()  { return (left = new Root); }
    inline Root* add_right() { return (right = new Root); }
};

struct Tree
{
    Root* head;
    Tree() : head{nullptr} {}
    ~Tree() { if(head)  delete head; }
    inline void add_head() { head = new Root; }
    void clear()
    {
        delete head;
        head = nullptr;
    }
};

int main()
{
    char mode, variable;
    string expression, variables; //"variables" is a set of variables without duplicates.
    bool tab[128];
    Tree tree;

    do
    {
        variables = "";
        cin >> mode;
        switch(mode)
        {
        case 'T':
            standard_fun(tree, expression, variables, tab);
            cout << ( tautology(tree,tab,variables.data()) ? "TAK\n" : "NIE\n" );
            break;
        case 'P':
            standard_fun(tree, expression, variables, tab);
            cout << how_many_true(tree,tab,variables.data()) << '\n';
            break;
        case 'F':
            standard_fun(tree, expression, variables, tab);
            cout << ( pow(2,variables.size())-how_many_true(tree,tab,variables.data()) ) << '\n';
            break;
        case '1':
        case '0':
            cin >> variable; //A letter from expression that will be change to true(1) or false(0).
            standard_fun(tree, expression, variables, tab);
            tab[static_cast<int>(variable)] = (mode=='0' ? false : true);
            variables.erase(variables.find(variable),1);
            cout << ( tautology(tree,tab,variables.data()) ? "TAK\n" : "NIE\n" );
            break;
        default:
            break;
        }
        tree.clear();
    } while(mode != 'W');

    return 0;
}

void make_tree(Tree& tree, const char* exp)
{
    tree.add_head();
    while(*exp == '~')
    {
        tree.head->neg++;
        exp++;
    }
    if(*exp == '(')
        make_root(tree.head, ++exp);
    else
        tree.head->symbol = *exp;
}

const char* make_root(Root* root, const char* exp)
{
    exp = make_one_side(root->add_left(), exp);
    root->symbol = *exp++;
    return make_one_side(root->add_right(), exp);
}

const char* make_one_side(Root* root, const char* ptr)
{
    while(*ptr++ == '~')
        root->neg++;
    if(*(--ptr) == '(')
    {
        ptr = make_root(root, ++ptr);
        while(*ptr == ')')  ptr++;
    }
    else
        root->symbol = *ptr++;

    return ptr;
}

void take_variables(const Root* root, string& var)
{
    if(root->symbol<'a' || root->symbol>'z')
    {
        take_variables(root->left, var);
        take_variables(root->right, var);
    }
    else if( var.find(root->symbol) == string::npos )
        var += root->symbol;
}

void standard_fun(Tree& tree, string& expression, string& variables, bool* tab)
{
    cin >> expression;
    make_tree(tree, expression.data());
    take_variables(tree.head, variables);
}

bool if_true(const Root* root, const bool* tab)
{
    bool result;

    switch(root->symbol)
    {
        case '|':   result = (if_true(root->left,tab) || if_true(root->right,tab));     break;
        case '&':   result = (if_true(root->left,tab) && if_true(root->right,tab));     break;
        case '>':   result = (!if_true(root->left,tab) || if_true(root->right,tab));    break;
        case '=':   result = (if_true(root->left,tab) == if_true(root->right,tab));     break;
        default:    result = tab[static_cast<int>(root->symbol)];   break;
    }

    return ( (root->neg%2) ? !result : result );
}

int how_many_true(const Tree& tree, bool* tab, const char* var)
{
    if(*var)
    {
    //We check the evaluations in lexicographic order.
        tab[static_cast<int>(*var)] = false;
        int result = how_many_true(tree,tab,var+1);
        tab[static_cast<int>(*var)] = true;
        result += how_many_true(tree,tab,var+1);
        return result;
    }
    else
        return ( if_true(tree.head, tab) ? 1 : 0 );
}

//This function work as how_many_true, but break if found "false tree".
bool tautology(const Tree& tree, bool* tab, const char* var)
{
    if(*var)
    {
    //We check the evaluations in lexicographic order.
        tab[static_cast<int>(*var)] = false;
        if( !(tautology(tree,tab,var+1)) )
            return false;

        tab[static_cast<int>(*var)] = true;
        return tautology(tree,tab,var+1);
    }
    else
        return if_true(tree.head, tab);
}
