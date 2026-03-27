void main() {
  // (String, int) minji = ('민지', 20);
  //
  // // ('민지', 20) 출력
  // print(minji);

  // (String, int, bool) minji = ('민지', 20, true);
  // // 민지
  // print(minji.$1);
  //
  // // 20
  // print(minji.$2);
  //
  // // true
  // print(minji.$3);

  // Named Parameter 형태로 Record를 선언하는 방법이다.
  // 다른 Named Parameter와 마찬가지로 순서는 상관이 없어진다.
  // ({String name, int age}) minji = (name: '민지', age: 20);
  //
  // // (age: 20, name: 민지) 출력
  // print(minji);
  // Named Parameter 형태로 Record를 선언하는 방법이다.
  // 다른 Named Parameter와 마찬가지로 순서는 상관이 없어진다.
  ({String name, int age}) minji = (name: '민지', age: 20);

  // (age: 20, name: 민지) 출력
  print(minji);

}
