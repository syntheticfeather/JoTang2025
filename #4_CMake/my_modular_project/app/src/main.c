#include "libmath/math_utils.h"
#include "libprint/print_utils.h"

int main() {
    int a = 5, b = 3;
    
    int sum = add(a, b);
    print_number(sum);
    
    int product = multiply(a, b);
    print_number(product);
    
    print_message("Hello from modular CMake project!");
    
    return 0;
}