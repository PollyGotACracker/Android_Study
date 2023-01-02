package com.polly.numbergame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.polly.numbergame.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    /**
     * private
     *  MainActivity 에 포함된 함수, method 들에서만
     *  변수에 접근(읽기, 쓰기)할 수 있다.
     *
     * 접근제한자
     *  변수나, 함수 등을 은닉하여 다른 곳에서 보거나
     *  값을 변경하는 것을 허용, 제한하는 키워드
     *  public : 제한 X  (기본값)
     *  internal : 같은 클래스 또는 모듈 내에서만
     *  protected : 같은 클래스 및 상속받은 클래스, 또는 같은 모듈 및 상속받은 모듈
     *  private : 같은 클래스 내부에서만
     *
     * lateinit var : 변수의 초기화(값 할당)를 잠시 늦춤
     * Kotlin 은 변수를 선언함과 동시에 초기화(값 할당)을 하는 것이 원칙이다.
     * 하지만 경우에 따라 선언부와 초기화 코드를 분리할 때 lateinit var 키워드로 시작한다.
     *
     * 프로젝트 gradle 설정에 viewBinding 항목을 true 로 enabled 해주면
     * layout / *.xml 파일들이 자동으로 viewBinding Class 로 생성된다.
     * ViewBinding Class 는 xml 파일에 설정된 모든 view 를 요소로 포함하고 있다.
     * * grable binding 설정 :
     *      Gradle Scripts > build.gradle(Module...)
     *          viewBinding {
     *          enabled true
     *          }
     */
    
    // top-level 에서 접근제한자 정의
    private lateinit var binding: ActivityMainBinding
    private var rndNumber = 0
    private val MAX_NUM = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // layout / activity_main.xml 과 연결하는 코드
        // binding 을 사용할 수 있도록 만들어주는 코드
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 1 부터 MAX_NUM 까지 정수 중에서 임의 숫자 한개를 추출하여
        // rndNumber 에 저장하라
        if (rndNumber === 0)
            rndNumber = (1 .. MAX_NUM).random()

        // setContentView(R.layout.activity_main)
        // R.layout... 또는 R.id... 를 쓰지 않고 binding 으로 조작
        // binding 으로 연결된 layout / *.xml 파일을 화면에 그려라
        setContentView(binding.root)

        // %1$d ... %2$d 형식으로 만든 문자열에 대하여
        // 첫번째 %1$d 항목에 1 숫자를 대입하고
        // 두번째 %2$d 항목에 MAX_NUM 변수의 값을 대입하여 문자열 완성
        // %1$d : format 형식, %1 첫번째 항목, $d 십진수
        // %1$s : 첫번째 항목이 문자열이라는 의미
        binding.txtNumberLayout.hint = getString(R.string.input_hint, 1, MAX_NUM)

        binding.btnRndNumber.setOnClickListener{
            rndNumber = (1 .. MAX_NUM).random()
            Snackbar
                .make(it,
                    // String.xml 에 설정된 text 값 가져오기
                    getString(R.string.game_restart),
                    Snackbar.LENGTH_LONG)
                .show()
        }

        binding.btnOk.setOnClickListener{ numberOk(it) }
        // EditText 입력 후 enter event
        binding.txtNumber.setOnEditorActionListener{view, keyCode, event ->
            numberOk(view)
            // input box 를 클릭하면 화면에 키보드가 보이는 상태
            // EditActionListener 이벤트를 설정하지 않으면
            // Enter 를 클릭했을 때 자동으로 키보드가 감춰진다.
            // EditActionListener 이벤트를 설정하고 가장 마지막에
            // 일반적으로 true(return true) 를 사용하는데
            // true 를 return 하는 것은 키보드 상태를 그대로 유지하라 라는 표현
            // 키보드가 화면에 보이는 상태에서는 Snackbar 를 표시할 수 없다.
            // Snackbar 를 보이게 하기 위해서는 끝에서 false 를 사용한다.
            true
        }
    }  // end OnCreate

    private fun numberOk (view: View){
        // input 에 입력된 문자열 추출
        val number = binding.txtNumber.text.toString()
        var message = getString(R.string.input_number)
        var ret = true
        try{
            if(number.toInt() < 1 || number.toInt() > MAX_NUM ) {
                message = getString(R.string.number_nan, number.toInt(), 1, MAX_NUM)
                ret = false
            }
            // 숫자가 아닌 값을 숫자형으로 변환하려 할 때 발생하는 exception
        } catch(e: NumberFormatException) {
            message = getString(R.string.number_fail)
            ret = false
        }
        // ref 가 false 일 경우 실행
        if (!ret) {
            Snackbar
                .make(
                    view,  // 어디에 부착하여, TextInputEdit 에 부착
                    message, // 무엇을
                    Snackbar.LENGTH_LONG  // 얼마동안
                )
                .show()  // Snackbar 보이기
        }

        // 유효성 검사 완료
        if(ret) {
            val resultIntent = Intent(
                this@MainActivity,
                ResultActivity::class.java)
                .apply {
                    putExtra("rndNumber", rndNumber)
                    putExtra("inputNumber", number.toInt())
                }
            startActivity(resultIntent)
        }
    }



}
