void main() {
  final numbers = [1, 2, 3, 4, 5, 6, 7, 8];

  // spread operator를 사용하게 되면 중간의 값들을 버릴 수 있다.
  final [x, y, ..., z] = numbers;

  // 1 출력
  print(x);

  // 2 출력
  print(y);

  // 8 출력
  print(z);

  final minjiMap = {'name': '민지', 'age': 19};
  // Map의 구조와 똑같은 구조로 Destructuring하면 된다.
  final {'name': name, 'age': age} = minjiMap;

  // name: 민지
  print('name: $name');

  // age: 19
  print('age: $age');
}
