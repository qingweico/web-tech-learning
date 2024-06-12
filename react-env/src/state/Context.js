// 使用 Context 深层传递参数
import Heading from './Heading.js';
import Section from './Section.js';

export default function Page() {
    return (
        <Section>
            <Heading>大标题</Heading>
            <Section>
                <Heading>一级标题</Heading>
                <Heading>一级标题</Heading>
                <Heading>一级标题</Heading>
                <Section>
                    <Heading>二级标题</Heading>
                    <Heading>二级标题</Heading>
                    <Heading>二级标题</Heading>
                    <Section>
                        <Heading>三级标题</Heading>
                        <Heading>三级标题</Heading>
                        <Heading>三级标题</Heading>
                    </Section>
                </Section>
            </Section>
        </Section>
    );
}
